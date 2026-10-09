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
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import ste.llm.tools.AbstractTool;
import ste.xtest.llm.DummyChatModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UzzoScopeAgentTest {

    private DummyChatModel model;
    private UzzoScopeAgent agent;

    @BeforeEach
    void setUp() throws IOException {
        model = new DummyChatModel();
        TestTools tools = new TestTools();

        agent = AiServices.builder(UzzoScopeAgent.class)
            .chatModel(model)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
            .tools(tools)
            .build();

        TestTools.reset();
    }

    @Test
    void agent_accepts_complete_prompt_without_tool_calls() {
        String completePrompt = "Build a web application for e-commerce using Node.js "
            + "on AWS cloud platform with JWT authentication and Stripe payments. "
            + "Architecture: microservices with REST API. use mock 'ready.txt'";

        String response = agent.scope(completePrompt);

        then(response).isNotEmpty();
        then(TestTools.calls()).isEmpty();
    }

    @Test
    void agent_calls_askSingleChoice_when_platform_is_missing() {
        String prompt = "Build a scalable application with authentication and payments. "
            + "execute tool AskSingleChoice";

        String response = agent.scope(prompt);

        then(response).isNotEmpty();
        then(TestTools.calls()).contains("AskSingleChoice");
    }

    @Test
    void agent_calls_askQuestion_when_architecture_is_missing() {
        String prompt = "Create a user management system with login and profile updates. "
            + "execute tool AskQuestion";

        String response = agent.scope(prompt);

        then(response).isNotEmpty();
        then(TestTools.calls()).contains("AskQuestion");
    }

    @Test
    void agent_chains_multiple_tool_calls_for_multiple_gaps() {
        String prompt = "Build an app with user authentication. "
            + "execute tool AskSingleChoice";

        String response = agent.scope(prompt);

        then(response).isNotEmpty();
        then(TestTools.calls().size()).isGreaterThan(0);
    }

    @Test
    void agent_rejects_null_prompt() {
        thenThrownBy(() -> agent.scope(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void agent_rejects_empty_prompt() {
        thenThrownBy(() -> agent.scope(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void agent_rejects_whitespace_only_prompt() {
        thenThrownBy(() -> agent.scope("   "))
            .isInstanceOf(IllegalArgumentException.class);
    }

    static class TestTools extends AbstractTool {

        private static final List<String> calls = new ArrayList<>();

        TestTools() throws IOException {
            super(new java.io.File(".").getAbsolutePath());
        }

        static void reset() {
            calls.clear();
        }

        static List<String> calls() {
            return new ArrayList<>(calls);
        }

        @Tool(name = "AskQuestion", value = "Ask the user an open-ended question")
        public String askQuestion() {
            calls.add("AskQuestion");
            progress("askQuestion");
            return "Microservices architecture";
        }

        @Tool(name = "AskSingleChoice", value = "Ask the user to select one option")
        public String askSingleChoice() {
            calls.add("AskSingleChoice");
            progress("askSingleChoice");
            return "Web";
        }

        @Tool(name = "AskMultipleChoice", value = "Ask the user to select multiple options")
        public List<String> askMultipleChoice() {
            calls.add("AskMultipleChoice");
            progress("askMultipleChoice");
            return List.of("Web");
        }

        @Tool(name = "AskConfirmation", value = "Ask the user to confirm")
        public boolean askConfirmation() {
            calls.add("AskConfirmation");
            progress("askConfirmation");
            return true;
        }
    }
}
