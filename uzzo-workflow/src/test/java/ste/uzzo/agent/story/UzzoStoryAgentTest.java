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
package ste.uzzo.agent.story;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.io.IOException;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.uzzo.agent.AgentSystemMessage;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

public class UzzoStoryAgentTest {

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;
    private UzzoStoryAgent agent;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);

        agent = AiServices.builder(UzzoStoryAgent.class)
            .streamingChatModel(model)
            .systemMessageProvider(chatMemoryId ->
                AgentSystemMessage.loadSkill("uzz/skills/story/SKILL.md"))
            .build();
    }

    @Test
    void agent_system_prompt_comes_from_shared_skill_without_frontmatter() {
        final TokenStream response = agent._stories_("Generate stories from the provided scope.")
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors();
        response.start();

        final ChatModelRequestContext request = listener.lastRequestContext.get();
        final ChatMessage message = request.chatRequest().messages().get(0);

        then(response).isNotNull();
        then(message.type()).isEqualTo(ChatMessageType.SYSTEM);
        then(((SystemMessage)message).text()).startsWith("# Skill: Generate User Stories from Scope");
        then(((SystemMessage)message).text()).contains("## Execution Rules & Standards");
        then(((SystemMessage)message).text()).doesNotContain("name: user-story-generator");
    }

    @Test
    void agent_rejects_null_request() {
        thenThrownBy(() -> agent.stories(null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void agent_rejects_empty_request() {
        thenThrownBy(() -> agent.stories(""))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void agent_rejects_whitespace_only_request() {
        thenThrownBy(() -> agent.stories("   "))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
