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

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.uzzo.agent.UzzoAgentBuilder;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

/**
 * US-000009 - the agent can ask the user for a yes/no confirmation through the
 * AskConfirmation tool.
 *
 * The tool call is driven by DummyChatModel, which passes a JSON object given
 * after "with arguments:" to the tool as it is (xtest 3.14.1 or later).
 */
public class AskConfirmationToolTest {

    private static final String QUESTION = "Shall I overwrite the existing scope?";

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);
    }

    @Test
    void confirmation_tool_receives_the_question_and_returns_true_when_the_user_confirms() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.confirmation = true;

        ask(tools);

        then(tools.calls).containsExactly(AskTools.ASK_CONFIRMATION);
        then(tools.question).isEqualTo(QUESTION);
        then(toolResult()).isEqualTo("true");
    }

    @Test
    void confirmation_tool_returns_false_when_the_user_declines() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.confirmation = false;

        ask(tools);

        then(tools.calls).containsExactly(AskTools.ASK_CONFIRMATION);
        then(toolResult()).isEqualTo("false");
    }

    @Test
    void confirmation_without_an_answer_tells_the_model_there_is_no_answer() throws IOException {
        final PresetAskTools tools = new PresetAskTools();
        tools.failure = new NoAnswerException();

        ask(tools);

        then(tools.calls).containsExactly(AskTools.ASK_CONFIRMATION);
        then(toolResult()).isEqualTo("<NO ANSWER PROVIDED>");
    }

    // --------------------------------------------------------- private methods

    private void ask(final AskTools tools) {
        UzzoAgentBuilder.ScopeAgentBuilder()
            .streamingChatModel(model)
            .tools(tools)
            .build()
            .scope("Build a scalable application. execute tool " + AskTools.ASK_CONFIRMATION
                + " with arguments: {\"question\": \"" + QUESTION + "\"}")
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
