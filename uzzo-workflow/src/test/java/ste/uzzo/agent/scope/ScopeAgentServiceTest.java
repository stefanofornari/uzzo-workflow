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
package ste.uzzo.agent.scope;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import ste.llm.tools.AbstractTool;
import ste.xtest.llm.DummyStreamingChatModel;
import ste.uzzo.agent.tools.AskTools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for ScopeAgentService prompt intake validation.
 *
 * Acceptance Criteria:
 * 1. Developer submits a well-formed project prompt → accepted without error
 * 2. Developer submits an empty prompt → rejected with error and remains waiting
 */
public class ScopeAgentServiceTest {

    private DummyStreamingChatModel model;
    private UzzoScopeAgent agent;

    @BeforeEach
    void setUp() throws IOException {
        model = new DummyStreamingChatModel();
        MockAskTools tools = new MockAskTools();

        agent = agent = this.agent = AiServices.builder(UzzoScopeAgent.class)
            .streamingChatModel(model)
            .tools(tools)
            .build();

        tools.reset();
    }

    @Test
    void agent_accepts_well_formed_prompt() {
        String prompt = "Create a REST API for managing user profiles with JWT authentication. "
            + "use mock 'ready.txt'";

        TokenStream response = agent.scope(prompt);

        then(response).isNotNull();
    }

    @Test
    void agent_rejects_empty_prompt() {
        thenThrownBy(() -> agent.scope(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("non blank");
    }

    @Test
    void agent_rejects_whitespace_only_prompt() {
        thenThrownBy(() -> agent.scope("   \t\n   "))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("non blank");
    }

    @Test
    void agent_rejects_null_prompt() {
        thenThrownBy(() -> agent.scope(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("non blank");
    }

    @Test
    void agent_remains_waiting_after_rejected_prompt() {
        try {
            agent.scope("");
        } catch (IllegalArgumentException e) {
            // Expected rejection
        }

        String validPrompt = "Create a mobile app for tracking expenses. "
            + "use mock 'ready.txt'";
        agent.scope(validPrompt);

        then(true).isTrue(); // Test passes if no exception thrown
    }

    static class MockAskTools extends AbstractTool implements AskTools {

        private static final List<String> calls = new ArrayList<>();

        MockAskTools() throws IOException {
            super(new java.io.File(".").getAbsolutePath());
        }

        void reset() {
            calls.clear();
        }

        @Override
        @Tool(name = "AskQuestion", value = "Ask the user an open-ended question")
        public String askQuestion(String question) {
            calls.add("AskQuestion");
            return "Microservices architecture";
        }

        @Override
        @Tool(name = "AskSingleChoice", value = "Ask the user to select one option")
        public String askSingleChoice(String question, List<String> options) {
            calls.add("AskSingleChoice");
            return options.get(0);
        }

        @Override
        @Tool(name = "AskMultipleChoice", value = "Ask the user to select multiple options")
        public List<String> askMultipleChoice(String question, List<String> options) {
            calls.add("AskMultipleChoice");
            return List.of(options.get(0));
        }

        @Override
        @Tool(name = "AskConfirmation", value = "Ask the user to confirm")
        public boolean askConfirmation(String question) {
            calls.add("AskConfirmation");
            return true;
        }
    }
}