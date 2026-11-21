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

import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.skills.Skill;
import dev.langchain4j.skills.Skills;

/**
 * Makes the uzzo-kb skill available to an agent through the LangChain4j Skills
 * tool mode: the skill catalogue goes in the system message and the
 * <code>activate_skill</code> tool lets the model load the skill on demand.
 * The skill is loaded each time it is needed, like the skill of the agent,
 * so a missing skill fails when the agent is invoked.
 */
class KbSkill {

    static final String NAME = "uzzo-kb";
    static final String RESOURCE = "uzz/skills/kb/SKILL.md";

    private static final String DESCRIPTION =
        "Manage and structure knowledge base articles across library, project, and user story folders. "
        + "Use when creating, updating, or organizing markdown documentation, requirements, and user stories.";

    private final AgentSystemMessage skills;

    KbSkill(final AgentSystemMessage skills) {
        this.skills = skills;
    }

    /**
     * @return the text to append to the system message of an agent to tell the model which skills it can activate
     */
    String catalogue() {
        return "You have access to the following skills:\n" + load().formatAvailableSkills()
            + "\nWhen the user's request relates to one of these skills, activate it first using the `activate_skill` tool before proceeding.";
    }

    ToolProvider toolProvider() {
        return request -> load().toolProvider().provideTools(request);
    }

    private Skills load() {
        return Skills.from(
            Skill.builder()
                .name(NAME)
                .description(DESCRIPTION)
                .content(skills.loadSkill(RESOURCE))
                .build()
        );
    }
}
