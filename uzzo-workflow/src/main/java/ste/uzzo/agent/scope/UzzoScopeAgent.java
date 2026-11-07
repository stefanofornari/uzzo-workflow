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

import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import ste.lazyjava.safe.Safe;


/**
 * LangChain4j AI Service for the Scope Agent's gap analysis capabilities.
 *
 * <p>The agent analyzes developer prompts to identify missing or ambiguous
 * requirements before scoping begins. The system prompt is loaded from the
 * shared scope skill resource.
 */
public interface UzzoScopeAgent extends Safe {

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
    default TokenStream scope(final String userMessage) {
        requireNonBlank(userMessage, "userMessage");
        return _scope_(userMessage);
    }

    /**
     * Internal method called by {@link #scope(String)} after validation.
     *
     * <p>Annotated with LangChain4j decorators; implementations are auto-generated
     * by {@link dev.langchain4j.service.AiServices}.
     *
     * @param userMessage the validated user message
     * @return the agent's response token stream
     */
    TokenStream _scope_(@UserMessage String userMessage);
}
