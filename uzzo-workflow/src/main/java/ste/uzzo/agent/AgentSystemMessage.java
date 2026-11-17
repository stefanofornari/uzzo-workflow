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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Loads a skill and turns it into the system message of an agent. By default
 * skills are resolved from the classpath; hosts and tests can change where
 * they come from by overriding {@link #resolve(String)}.
 */
public class AgentSystemMessage {

    /**
     * Loads the skill stored in the given resource and returns its body, i.e.
     * without the leading YAML frontmatter.
     *
     * @param skillResource the name of the resource holding the skill
     * @return the skill text without frontmatter
     * @throws IllegalStateException if the resource cannot be resolved
     * @throws UncheckedIOException if the resource cannot be read
     */
    public String loadSkill(final String skillResource) {
        try (InputStream skill = resolve(skillResource)) {
            if (skill == null) {
                throw new IllegalStateException("Missing system prompt resource: " + skillResource);
            }

            return new String(skill.readAllBytes(), StandardCharsets.UTF_8)
                .replaceFirst("(?s)\\A---\\R.*?\\R---\\R+", "");
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read system prompt resource: " + skillResource, e);
        }
    }

    /**
     * Resolves a named resource to a stream. The default implementation looks
     * the resource up in the classpath of this class.
     *
     * @param resource the name of the resource to resolve
     * @return the stream of the resource, or null if it cannot be found
     */
    protected InputStream resolve(final String resource) {
        return AgentSystemMessage.class.getClassLoader().getResourceAsStream(resource);
    }
}
