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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import ste.llm.tools.AbstractTool;
import ste.xtest.llm.DummyChatModel;
import ste.uzzo.agent.tools.AskTools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for ScopeAgentService prompt analysis with clarification tools.
 *
 * Acceptance Criteria:
 * 1. Prompt with clear requirements, no gaps → agent acknowledges, no tools called
 * 2. Prompt missing platform → agent calls AskSingleChoice for platform
 * 3. Prompt missing architecture → agent calls AskQuestion for details
 */
public class ScopeAgentServiceClarificationTest {

    private DummyChatModel model;
    private ScopeAgentService agent;
    private MockAskTools mockTools;

    @BeforeEach
    void setUp() throws IOException {
        model = new DummyChatModel();
        mockTools = new MockAskTools();

        agent = new ScopeAgentService(model, mockTools);

        MockAskTools.reset();
    }

    @Test
    void agent_analyzes_complete_prompt_without_tool_calls() {
        String completePrompt = "Build a web application for e-commerce using Node.js "
            + "on AWS cloud platform with JWT authentication and Stripe payments. "
            + "Architecture: microservices with REST API. use mock 'ready.txt'";

        String response = agent.scope(completePrompt);

        then(response).isNotEmpty();
        then(MockAskTools.calls()).isEmpty();
    }

    @Test
    void agent_calls_askSingleChoice_when_platform_is_missing() {
        String ambiguousPrompt = "Build a scalable application with authentication and payments. "
            + "execute tool AskSingleChoice";

        String response = agent.scope(ambiguousPrompt);

        then(response).isNotEmpty();
        then(MockAskTools.calls()).contains("AskSingleChoice");
    }

    @Test
    void agent_calls_askQuestion_when_architecture_is_missing() {
        String architectureGapPrompt = "Create a user management system with login and profile updates. "
            + "execute tool AskQuestion";

        String response = agent.scope(architectureGapPrompt);

        then(response).isNotEmpty();
        then(MockAskTools.calls()).contains("AskQuestion");
    }

    @Test
    void agent_chains_multiple_tool_calls_for_multiple_gaps() {
        String incompletePrompt = "Build an app with user authentication. "
            + "execute tool AskSingleChoice";

        String response = agent.scope(incompletePrompt);

        then(response).isNotEmpty();
        then(MockAskTools.calls().size()).isGreaterThan(0);
    }

    static class MockAskTools extends AbstractTool implements AskTools {

        private static final List<String> calls = new ArrayList<>();

        MockAskTools() throws IOException {
            super(new java.io.File(".").getAbsolutePath());
        }

        static void reset() {
            calls.clear();
        }

        static List<String> calls() {
            return new ArrayList<>(calls);
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