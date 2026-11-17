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
package ste.uzzo.agent;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.io.IOException;
import java.io.InputStream;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageType;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.service.TokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.uzzo.agent.code.UzzoCodeAgent;
import ste.uzzo.agent.scope.UzzoScopeAgent;
import ste.uzzo.agent.story.UzzoStoryAgent;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

/**
 * US-000006 - each builder of {@link UzzoAgentBuilder} binds its agent to the
 * system prompt taken from its own skill.
 */
public class UzzoAgentBuilderTest {

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);
    }

    @Test
    void scope_builder_binds_agent_to_the_scope_skill() {
        final UzzoScopeAgent agent = UzzoAgentBuilder.ScopeAgentBuilder()
            .streamingChatModel(model)
            .build();

        invoke(agent._scope_("Create a REST API for managing user profiles. use mock 'ready.txt'"));

        then(systemPrompt()).startsWith("# Skill: Scope").contains("## Pipeline");
    }

    @Test
    void story_builder_binds_agent_to_the_story_skill() {
        final UzzoStoryAgent agent = UzzoAgentBuilder.StoryAgentBuilder()
            .streamingChatModel(model)
            .build();

        invoke(agent._stories_("Generate stories from the provided scope."));

        then(systemPrompt())
            .startsWith("# Skill: Generate User Stories from Scope")
            .contains("## Execution Rules & Standards");
    }

    @Test
    void code_builder_binds_agent_to_the_code_skill() {
        final UzzoCodeAgent agent = UzzoAgentBuilder.CodeAgentBuilder()
            .streamingChatModel(model)
            .build();

        invoke(agent._implement_("Implement user story auth/US-000012"));

        then(systemPrompt())
            .startsWith("# Skill: Implement User Story")
            .contains("## Execution Pipeline");
    }

    @Test
    void scope_agent_fails_when_the_scope_skill_is_missing() {
        final UzzoScopeAgent agent = UzzoAgentBuilder.ScopeAgentBuilder(withoutSkills())
            .streamingChatModel(model)
            .build();

        thenThrownBy(() -> agent._scope_("Create a REST API"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/scope/SKILL.md");
    }

    @Test
    void story_agent_fails_when_the_story_skill_is_missing() {
        final UzzoStoryAgent agent = UzzoAgentBuilder.StoryAgentBuilder(withoutSkills())
            .streamingChatModel(model)
            .build();

        thenThrownBy(() -> agent._stories_("Generate stories from the provided scope."))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/story/SKILL.md");
    }

    @Test
    void code_agent_fails_when_the_code_skill_is_missing() {
        final UzzoCodeAgent agent = UzzoAgentBuilder.CodeAgentBuilder(withoutSkills())
            .streamingChatModel(model)
            .build();

        thenThrownBy(() -> agent._implement_("Implement user story auth/US-000012"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/code/SKILL.md");
    }

    // --------------------------------------------------------- private methods

    private AgentSystemMessage withoutSkills() {
        return new AgentSystemMessage() {
            @Override
            protected InputStream resolve(final String resource) {
                return null;
            }
        };
    }

    private void invoke(final TokenStream stream) {
        final TokenStream response = stream
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors();
        response.start();
    }

    private String systemPrompt() {
        final ChatMessage message = listener.lastRequestContext.get().chatRequest().messages().get(0);

        then(message.type()).isEqualTo(ChatMessageType.SYSTEM);

        return ((SystemMessage)message).text();
    }
}
