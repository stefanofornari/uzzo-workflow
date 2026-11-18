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

import java.io.IOException;
import java.util.List;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.uzzo.agent.UzzoAgentBuilder;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

/**
 * US-000008 - the agent can offer the user a choice (single or multiple) through
 * the AskSingleChoice and AskMultipleChoice tools.
 *
 * The tool calls are driven by DummyChatModel, which passes a JSON object given
 * after "with arguments:" to the tool as it is (xtest 3.14.1 or later).
 */
public class AskChoiceToolsTest {

    private static final String QUESTION = "Which platform?";
    private static final List<String> OPTIONS = List.of("Web", "Mobile", "Desktop");
    private static final String ARGUMENTS =
        "{\"question\": \"Which platform?\", \"options\": [\"Web\", \"Mobile\", \"Desktop\"]}";

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);
    }

    @Test
    void single_choice_tool_receives_question_and_options_and_returns_the_selected_option() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.singleChoice = "Mobile";

        ask(tools, AskTools.ASK_SINGLE_CHOICE);

        then(tools.calls).containsExactly(AskTools.ASK_SINGLE_CHOICE);
        then(tools.question).isEqualTo(QUESTION);
        then(tools.options).isEqualTo(OPTIONS);
        then(toolResult()).isEqualTo("Mobile");
    }

    @Test
    void multiple_choice_tool_receives_question_and_options_and_returns_the_selected_options() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.multipleChoice = List.of("Web", "Desktop");

        ask(tools, AskTools.ASK_MULTIPLE_CHOICE);

        then(tools.calls).containsExactly(AskTools.ASK_MULTIPLE_CHOICE);
        then(tools.question).isEqualTo(QUESTION);
        then(tools.options).isEqualTo(OPTIONS);
        then(toolResult()).contains("Web").contains("Desktop").doesNotContain("Mobile");
    }

    @Test
    void single_choice_without_an_answer_tells_the_model_there_is_no_answer() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.failure = new NoAnswerException();

        ask(tools, AskTools.ASK_SINGLE_CHOICE);

        then(tools.calls).containsExactly(AskTools.ASK_SINGLE_CHOICE);
        then(toolResult()).isEqualTo("<NO ANSWER PROVIDED>");
    }

    @Test
    void multiple_choice_without_an_answer_tells_the_model_there_is_no_answer() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.failure = new NoAnswerException();

        ask(tools, AskTools.ASK_MULTIPLE_CHOICE);

        then(tools.calls).containsExactly(AskTools.ASK_MULTIPLE_CHOICE);
        then(toolResult()).isEqualTo("<NO ANSWER PROVIDED>");
    }

    // --------------------------------------------------------- private methods

    private void ask(final AskTools tools, final String tool) {
        UzzoAgentBuilder.ScopeAgentBuilder()
            .streamingChatModel(model)
            .tools(tools)
            .build()
            .scope("Build a scalable application. execute tool " + tool + " with arguments: " + ARGUMENTS)
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();
    }

    private String toolResult() {
        return listener.lastRequestContext.get().chatRequest().messages().stream()
            .filter(ToolExecutionResultMessage.class::isInstance)
            .map(message -> ((ToolExecutionResultMessage)message).text())
            .findFirst()
            .orElse(null);
    }
}
