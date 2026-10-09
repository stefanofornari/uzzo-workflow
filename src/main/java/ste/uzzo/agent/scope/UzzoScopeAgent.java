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

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * LangChain4j AI Service for the Scope Agent's gap analysis capabilities.
 *
 * <p>The agent analyzes developer prompts to identify missing or ambiguous
 * requirements before scoping begins. The system prompt is loaded from a
 * resource file rather than defined inline.
 */
public interface UzzoScopeAgent {

    /**
     * Processes a user message in the gap analysis dialogue with input validation.
     *
     * <p>On the first call, accepts a project/feature prompt, validates it,
     * and initiates gap analysis. On subsequent calls, continues the dialogue
     * based on the accumulated conversation history.
     *
     * @param userMessage the developer's message (first call: project prompt)
     * @return the agent's response
     * @throws IllegalArgumentException if userMessage is null, empty, or whitespace-only
     */
    default String scope(final String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            throw new IllegalArgumentException("prompt cannot be null or empty");
        }
        return _scope_(userMessage);
    }

    /**
     * Loads the system prompt from the resource file.
     *
     * <p>The prompt guides the agent's gap analysis behavior, including when
     * to use clarifying tools and how to partition requirements.
     *
     * @return the system prompt text
     * @throws IllegalStateException if the resource file is not found
     */
    @SystemMessage
    default String systemMessage() {
        try (var is = getClass().getResourceAsStream("uzzo-scope-agent-system-prompt.txt")) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: uzzo-scope-agent-system-prompt.txt");
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Internal method called by {@link #scope(String)} after validation.
     *
     * <p>Annotated with LangChain4j decorators; implementations are auto-generated
     * by {@link dev.langchain4j.service.AiServices}.
     *
     * @param userMessage the validated user message
     * @return the agent's response
     */
    String _scope_(@UserMessage String userMessage);
}
