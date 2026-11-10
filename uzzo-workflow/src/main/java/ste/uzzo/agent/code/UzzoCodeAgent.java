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
package ste.uzzo.agent.code;

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import ste.lazyjava.safe.Safe;

/**
 * LangChain4j AI Service for implementing user stories from specification to tested code.
 *
 * <p>The system prompt is loaded from the shared implement-user-story skill resource.
 */
public interface UzzoCodeAgent extends Safe {

    /**
     * Processes a request to implement a user story.
     *
     * @param userMessage the user's request containing the user story ID and context
     * @return the agent's response token stream
     * @throws IllegalArgumentException if userMessage is null, empty, or whitespace-only
     */
    default TokenStream implement(final String userMessage) {
        requireNonBlank(userMessage, "userMessage");
        return _implement_(userMessage);
    }

    /**
     * Internal method called by {@link #implement(String)} after validation.
     *
     * @param userMessage the validated user message
     * @return the agent's response token stream
     */
    TokenStream _implement_(@UserMessage String userMessage);
}