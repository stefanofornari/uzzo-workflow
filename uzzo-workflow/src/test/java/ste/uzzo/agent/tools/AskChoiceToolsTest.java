/*
 * Copyright 2026 the original author or authors from the Uzzo and Uzzo Workflow project
 * (https://stefanofornari.github.io/uzzo-workflow).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ste.uzzo.agent.tools;

import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import org.junit.jupiter.api.Test;
import ste.llm.tools.AbstractTool;
import ste.uzzo.agent.UzzoAgentBuilder;

/**
 * US-000008 - the agent can offer the user a choice (single or multiple) through
 * the AskSingleChoice and AskMultipleChoice tools.
 *
 * The tool calls are scripted with {@link ScriptedToolCallModel} instead of
 * DummyChatModel because the latter can only pass a single "prompt" argument,
 * while these tools take a question and a list of options.
 */
public class AskChoiceToolsTest {

    private static final String QUESTION = "Which platform?";
    private static final String ARGUMENTS =
        "{\"question\":\"Which platform?\",\"options\":[\"Web\",\"Mobile\",\"Desktop\"]}";
    private static final List<String> OPTIONS = List.of("Web", "Mobile", "Desktop");

    @Test
    void single_choice_tool_receives_question_and_options_and_returns_the_selected_option() throws IOException {
        final ChoiceAskTools tools = new ChoiceAskTools();
        tools.singleChoice = "Mobile";
        final ScriptedToolCallModel model = new ScriptedToolCallModel(AskTools.ASK_SINGLE_CHOICE, ARGUMENTS);

        ask(model, tools);

        then(tools.calls).containsExactly(AskTools.ASK_SINGLE_CHOICE);
        then(tools.question).isEqualTo(QUESTION);
        then(tools.options).isEqualTo(OPTIONS);
        then(model.toolResult()).isEqualTo("Mobile");
    }

    @Test
    void multiple_choice_tool_receives_question_and_options_and_returns_the_selected_options() throws IOException {
        final ChoiceAskTools tools = new ChoiceAskTools();
        tools.multipleChoice = List.of("Web", "Desktop");
        final ScriptedToolCallModel model = new ScriptedToolCallModel(AskTools.ASK_MULTIPLE_CHOICE, ARGUMENTS);

        ask(model, tools);

        then(tools.calls).containsExactly(AskTools.ASK_MULTIPLE_CHOICE);
        then(tools.question).isEqualTo(QUESTION);
        then(tools.options).isEqualTo(OPTIONS);
        then(model.toolResult()).contains("Web").contains("Desktop").doesNotContain("Mobile");
    }

    @Test
    void single_choice_without_an_answer_tells_the_model_there_is_no_answer() throws IOException {
        final ChoiceAskTools tools = new ChoiceAskTools();
        tools.failure = new NoAnswerException();
        final ScriptedToolCallModel model = new ScriptedToolCallModel(AskTools.ASK_SINGLE_CHOICE, ARGUMENTS);

        ask(model, tools);

        then(tools.calls).containsExactly(AskTools.ASK_SINGLE_CHOICE);
        then(model.toolResult()).isEqualTo("<NO ANSWER PROVIDED>");
    }

    @Test
    void multiple_choice_without_an_answer_tells_the_model_there_is_no_answer() throws IOException {
        final ChoiceAskTools tools = new ChoiceAskTools();
        tools.failure = new NoAnswerException();
        final ScriptedToolCallModel model = new ScriptedToolCallModel(AskTools.ASK_MULTIPLE_CHOICE, ARGUMENTS);

        ask(model, tools);

        then(tools.calls).containsExactly(AskTools.ASK_MULTIPLE_CHOICE);
        then(model.toolResult()).isEqualTo("<NO ANSWER PROVIDED>");
    }

    // --------------------------------------------------------- private methods

    private void ask(final StreamingChatModel model, final AskTools tools) {
        UzzoAgentBuilder.ScopeAgentBuilder()
            .streamingChatModel(model)
            .tools(tools)
            .build()
            .scope("Build a scalable application with authentication and payments.")
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();
    }

    // ----------------------------------------------------------- inner classes

    /**
     * A model that, on its first turn, asks to execute the given tool with the
     * given JSON arguments, and on the next turn just answers "done". It keeps
     * the requests it received.
     */
    static class ScriptedToolCallModel implements StreamingChatModel {

        private final String tool;
        private final String arguments;
        private final List<ChatRequest> requests = new ArrayList<>();

        ScriptedToolCallModel(final String tool, final String arguments) {
            this.tool = tool;
            this.arguments = arguments;
        }

        @Override
        public void doChat(final ChatRequest request, final StreamingChatResponseHandler handler) {
            requests.add(request);

            final boolean toolExecuted = request.messages().stream()
                .anyMatch(ToolExecutionResultMessage.class::isInstance);

            final AiMessage reply = toolExecuted
                ? AiMessage.from("done")
                : AiMessage.from(
                    ToolExecutionRequest.builder().id("1").name(tool).arguments(arguments).build()
                );

            handler.onCompleteResponse(ChatResponse.builder().aiMessage(reply).build());
        }

        /**
         * @return the text of the tool result sent back to the model, or null
         */
        String toolResult() {
            return requests.get(requests.size() - 1).messages().stream()
                .filter(ToolExecutionResultMessage.class::isInstance)
                .map(message -> ((ToolExecutionResultMessage)message).text())
                .findFirst()
                .orElse(null);
        }
    }

    /**
     * An AskTools implementation that records what it is asked and answers with
     * preset values, or fails with the preset exception.
     */
    static class ChoiceAskTools extends AbstractTool implements AskTools {

        final List<String> calls = new ArrayList<>();
        String question;
        List<String> options;
        String singleChoice;
        List<String> multipleChoice;
        RuntimeException failure;

        ChoiceAskTools() throws IOException {
            super(new File(".").getAbsolutePath());
        }

        @Override
        @Tool(name = ASK_QUESTION, value = "Ask the user an open-ended question")
        public String askQuestion(final String question) {
            calls.add(ASK_QUESTION);
            return "";
        }

        @Override
        @Tool(name = ASK_SINGLE_CHOICE, value = "Ask the user to select one option")
        public String askSingleChoice(final String question, final List<String> options) {
            calls.add(ASK_SINGLE_CHOICE);
            this.question = question;
            this.options = options;
            if (failure != null) {
                throw failure;
            }
            return singleChoice;
        }

        @Override
        @Tool(name = ASK_MULTIPLE_CHOICE, value = "Ask the user to select multiple options")
        public List<String> askMultipleChoice(final String question, final List<String> options) {
            calls.add(ASK_MULTIPLE_CHOICE);
            this.question = question;
            this.options = options;
            if (failure != null) {
                throw failure;
            }
            return multipleChoice;
        }

        @Override
        @Tool(name = ASK_CONFIRMATION, value = "Ask the user to confirm")
        public boolean askConfirmation(final String question) {
            calls.add(ASK_CONFIRMATION);
            return false;
        }
    }
}
