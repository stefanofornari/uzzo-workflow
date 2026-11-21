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
import java.util.List;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.service.TokenStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ste.uzzo.agent.tools.AskTools;
import ste.uzzo.agent.tools.PresetAskTools;
import ste.xtest.llm.DummyChatModelListener;
import ste.xtest.llm.DummyStreamingChatModel;

/**
 * US-000011 - every agent built by {@link UzzoAgentBuilder} can activate the
 * uzzo-kb skill through the LangChain4j Skills tool mode.
 */
public class UzzoKbSkillTest {

    private static final String KB_SKILL = "uzz/skills/kb/SKILL.md";

    private DummyStreamingChatModel model;
    private DummyChatModelListener listener;

    @BeforeEach
    void beforeEach() throws IOException {
        model = new DummyStreamingChatModel();
        listener = new DummyChatModelListener();
        model.addListener(listener);
    }

    @Test
    void scope_agent_lists_the_kb_skill_and_offers_activate_skill() {
        invoke(UzzoAgentBuilder.ScopeAgentBuilder().streamingChatModel(model).build()
            ._scope_("Create a REST API for managing user profiles."));

        then(systemPrompt()).startsWith("# Skill: Scope");
        thenKbSkillIsAvailable();
    }

    @Test
    void story_agent_lists_the_kb_skill_and_offers_activate_skill() {
        invoke(UzzoAgentBuilder.StoryAgentBuilder().streamingChatModel(model).build()
            ._stories_("Generate stories from the provided scope."));

        then(systemPrompt()).startsWith("# Skill: Generate User Stories from Scope");
        thenKbSkillIsAvailable();
    }

    @Test
    void code_agent_lists_the_kb_skill_and_offers_activate_skill() {
        invoke(UzzoAgentBuilder.CodeAgentBuilder().streamingChatModel(model).build()
            ._implement_("Implement user story auth/US-000012"));

        then(systemPrompt()).startsWith("# Skill: Implement User Story");
        thenKbSkillIsAvailable();
    }

    @Test
    void activating_the_kb_skill_gives_its_body_without_frontmatter_to_the_model() {
        UzzoAgentBuilder.ScopeAgentBuilder().streamingChatModel(model).build()
            .scope("Build an app. execute tool activate_skill with arguments: {\"skill_name\": \"uzzo-kb\"}")
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();

        then(toolResult())
            .isEqualTo(new AgentSystemMessage().loadSkill(KB_SKILL))
            .startsWith("# Knowledge Base Management")
            .doesNotContain("name: uzzo-kb");
    }

    @Test
    void tools_of_the_developer_keep_working_next_to_the_kb_skill() throws IOException {
        final PresetAskTools tools = new PresetAskTools();

        UzzoAgentBuilder.ScopeAgentBuilder().streamingChatModel(model).tools(tools).build()
            .scope("Build an app. execute tool AskQuestion")
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();

        then(tools.calls).containsExactly(AskTools.ASK_QUESTION);
        then(toolNames()).contains(AskTools.ASK_QUESTION, "activate_skill");
    }

    @Test
    void agent_fails_when_the_kb_skill_is_missing() {
        final AgentSystemMessage withoutKbSkill = new AgentSystemMessage() {
            @Override
            protected InputStream resolve(final String resource) {
                return KB_SKILL.equals(resource) ? null : super.resolve(resource);
            }
        };

        thenThrownBy(() ->
            UzzoAgentBuilder.ScopeAgentBuilder(withoutKbSkill).streamingChatModel(model).build()
                ._scope_("Create a REST API")
        )
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: " + KB_SKILL);
    }

    // --------------------------------------------------------- private methods

    private void invoke(final TokenStream stream) {
        stream
            .onPartialResponse(s -> {})
            .onCompleteResponse(r -> {})
            .ignoreErrors()
            .start();
    }

    private String systemPrompt() {
        return ((SystemMessage)listener.lastRequestContext.get().chatRequest().messages().get(0)).text();
    }

    private List<String> toolNames() {
        return listener.lastRequestContext.get().chatRequest().toolSpecifications().stream()
            .map(ToolSpecification::name)
            .toList();
    }

    private void thenKbSkillIsAvailable() {
        then(systemPrompt())
            .contains("<available_skills>")
            .contains("<name>uzzo-kb</name>");
        then(toolNames()).contains("activate_skill");
    }

    private String toolResult() {
        return listener.lastRequestContext.get().chatRequest().messages().stream()
            .filter(ToolExecutionResultMessage.class::isInstance)
            .map(message -> ((ToolExecutionResultMessage)message).text())
            .findFirst()
            .orElse(null);
    }
}
