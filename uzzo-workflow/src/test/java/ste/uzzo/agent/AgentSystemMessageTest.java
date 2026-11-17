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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * US-000005 - a missing or unreadable skill is reported with a clear error.
 */
public class AgentSystemMessageTest {

    @Test
    void loading_a_missing_skill_fails_with_a_message_naming_the_resource() {
        thenThrownBy(() -> new AgentSystemMessage().loadSkill("uzz/skills/missing/SKILL.md"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/missing/SKILL.md");
    }

    @Test
    void the_error_message_names_the_requested_resource_each_time() {
        thenThrownBy(() -> new AgentSystemMessage().loadSkill("uzz/skills/other/SKILL.md"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("Missing system prompt resource: uzz/skills/other/SKILL.md");
    }

    @Test
    void loading_an_unreadable_skill_fails_with_a_message_naming_the_resource() {
        final AgentSystemMessage message = new AgentSystemMessage() {
            @Override
            protected InputStream resolve(final String resource) {
                return new FailingInputStream();
            }
        };

        thenThrownBy(() -> message.loadSkill("uzz/skills/broken/SKILL.md"))
            .isInstanceOf(UncheckedIOException.class)
            .hasMessage("Unable to read system prompt resource: uzz/skills/broken/SKILL.md")
            .hasCauseInstanceOf(IOException.class);
    }

    @Test
    void loading_a_skill_uses_the_stream_returned_by_resolve() {
        final AgentSystemMessage message = new AgentSystemMessage() {
            @Override
            protected InputStream resolve(final String resource) {
                return new ByteArrayInputStream(
                    ("---\nname: test\n---\n# Skill: " + resource).getBytes(StandardCharsets.UTF_8)
                );
            }
        };

        then(message.loadSkill("custom/SKILL.md")).isEqualTo("# Skill: custom/SKILL.md");
    }

    // ----------------------------------------------------------- inner classes

    private static class FailingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("cannot read");
        }
    }
}
