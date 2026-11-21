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

import dev.langchain4j.service.AiServices;
import ste.uzzo.agent.scope.UzzoScopeAgent;
import ste.uzzo.agent.story.UzzoStoryAgent;
import ste.uzzo.agent.code.UzzoCodeAgent;

/**
 *
 */
public class UzzoAgentBuilder {

    private static final AgentSystemMessage SKILLS = new AgentSystemMessage();

    public static AiServices<UzzoScopeAgent> ScopeAgentBuilder() {
        return ScopeAgentBuilder(SKILLS);
    }

    /**
     * Same as {@link #ScopeAgentBuilder()}, but the skill is loaded through the given
     * {@link AgentSystemMessage}, so that a host can change where skills come from.
     */
    public static AiServices<UzzoScopeAgent> ScopeAgentBuilder(final AgentSystemMessage skills) {
        final KbSkill kb = new KbSkill(skills);
        return AiServices.builder(UzzoScopeAgent.class)
            .systemMessageProvider(
                chatMemoryId -> skills.loadSkill("uzz/skills/scope/SKILL.md") + "\n\n" + kb.catalogue()
            )
            .toolProvider(kb.toolProvider());
    }

    public static AiServices<UzzoStoryAgent> StoryAgentBuilder() {
        return StoryAgentBuilder(SKILLS);
    }

    /**
     * Same as {@link #StoryAgentBuilder()}, but the skill is loaded through the given
     * {@link AgentSystemMessage}, so that a host can change where skills come from.
     */
    public static AiServices<UzzoStoryAgent> StoryAgentBuilder(final AgentSystemMessage skills) {
        final KbSkill kb = new KbSkill(skills);
        return AiServices.builder(UzzoStoryAgent.class)
            .systemMessageProvider(
                chatMemoryId -> skills.loadSkill("uzz/skills/story/SKILL.md") + "\n\n" + kb.catalogue()
            )
            .toolProvider(kb.toolProvider());
    }

    public static AiServices<UzzoCodeAgent> CodeAgentBuilder() {
        return CodeAgentBuilder(SKILLS);
    }

    /**
     * Same as {@link #CodeAgentBuilder()}, but the skill is loaded through the given
     * {@link AgentSystemMessage}, so that a host can change where skills come from.
     */
    public static AiServices<UzzoCodeAgent> CodeAgentBuilder(final AgentSystemMessage skills) {
        final KbSkill kb = new KbSkill(skills);
        return AiServices.builder(UzzoCodeAgent.class)
            .systemMessageProvider(
                chatMemoryId -> skills.loadSkill("uzz/skills/code/SKILL.md") + "\n\n" + kb.catalogue()
            )
            .toolProvider(kb.toolProvider());
    }
}
