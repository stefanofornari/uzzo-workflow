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
import java.util.List;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.llm.tools.AbstractTool;
import ste.uzzo.agent.UzzoAgentBuilder;
import ste.uzzo.agent.scope.UzzoScopeAgent;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

/**
 * US-000010 - a NoAnswerException thrown by an AskTools implementation is
 * passed to the model as the result of the tool call.
 */
public class NoAnswerToolErrorTest {

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;

    @BeforeEach
    void beforeEach() {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);
    }

    @Test
    void default_no_answer_message_is_passed_to_the_model() throws IOException {
        ask(new NoAnswerAskTools(new NoAnswerException()));

        then(toolResults()).contains("<NO ANSWER PROVIDED>");
    }

    @Test
    void custom_no_answer_message_is_passed_to_the_model() throws IOException {
        ask(new NoAnswerAskTools(new NoAnswerException("the user skipped the question")));

        then(toolResults()).contains("the user skipped the question");
    }

    // --------------------------------------------------------- private methods

    private void ask(final AskTools tools) {
        final UzzoScopeAgent agent = UzzoAgentBuilder.ScopeAgentBuilder()
            .streamingChatModel(model)
            .tools(tools)
            .build();

        agent.scope("Build an app with user authentication. execute tool AskQuestion")
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();
    }

    private List<String> toolResults() {
        return listener.lastRequestContext.get().chatRequest().messages().stream()
            .filter(ToolExecutionResultMessage.class::isInstance)
            .map(message -> ((ToolExecutionResultMessage)message).text())
            .toList();
    }

    // ----------------------------------------------------------- inner classes

    /**
     * An AskTools implementation whose user never has an answer.
     */
    static class NoAnswerAskTools extends AbstractTool implements AskTools {

        private final NoAnswerException exception;

        NoAnswerAskTools(final NoAnswerException exception) throws IOException {
            super(new File(".").getAbsolutePath());
            this.exception = exception;
        }

        @Override
        @Tool(name = ASK_QUESTION, value = "Ask the user an open-ended question")
        public String askQuestion(final String question) {
            throw exception;
        }

        @Override
        @Tool(name = ASK_SINGLE_CHOICE, value = "Ask the user to select one option")
        public String askSingleChoice(final String question, final List<String> options) {
            throw exception;
        }

        @Override
        @Tool(name = ASK_MULTIPLE_CHOICE, value = "Ask the user to select multiple options")
        public List<String> askMultipleChoice(final String question, final List<String> options) {
            throw exception;
        }

        @Override
        @Tool(name = ASK_CONFIRMATION, value = "Ask the user to confirm")
        public boolean askConfirmation(final String question) {
            throw exception;
        }
    }
}
