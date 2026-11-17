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

import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import org.junit.jupiter.api.Test;

/**
 * US-000005 - a missing skill is reported with a clear error.
 */
public class AgentSystemMessageTest {

    @Test
    void loading_a_missing_skill_fails_with_a_message_naming_the_resource() {
        thenThrownBy(() -> AgentSystemMessage.loadSkill("uzz/skills/missing/SKILL.md"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/missing/SKILL.md");
    }

    @Test
    void the_error_message_names_the_requested_resource_each_time() {
        thenThrownBy(() -> AgentSystemMessage.loadSkill("uzz/skills/other/SKILL.md"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/other/SKILL.md");
    }
}
