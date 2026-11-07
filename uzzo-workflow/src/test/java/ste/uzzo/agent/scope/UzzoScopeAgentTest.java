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
import ste.uzzo.agent.AgentSystemMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

public class UzzoScopeAgentTest {

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;
    private UzzoScopeAgent agent;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);

        agent = AiServices.builder(UzzoScopeAgent.class)
            .streamingChatModel(model)
            .systemMessageProvider(chatMemoryId -> AgentSystemMessage.loadSkill("uzz/skills/scope/SKILL.md"))
            .build();
    }

    @Test
    void agent_system_prompt() {
        final String prompt = "Build a scalable application with authentication and payments. "
            + "execute tool AskSingleChoice";

        final TokenStream response = agent._scope_(prompt)
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors();
        response.start();

        final ChatModelRequestContext request = listener.lastRequestContext.get();
        final ChatMessage msg = request.chatRequest().messages().get(0);

        then(response).isNotNull();
        then(msg.type()).isEqualTo(ChatMessageType.SYSTEM);
        then(((SystemMessage)msg).text()).startsWith("# Skill: Scope Definition Reviewer");
        then(((SystemMessage)msg).text()).contains("## Goal");
        then(((SystemMessage)msg).text()).doesNotContain("name: scope-definition-reviewer");
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
}