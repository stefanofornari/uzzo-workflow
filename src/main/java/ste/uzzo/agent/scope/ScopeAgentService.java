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

import java.io.IOException;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import ste.uzzo.agent.tools.AskTools;

/**
 * Concrete provider for the {@link UzzoScopeAgent} LangChain4j AI Service.
 *
 * <p>Builds and configures the agent instance with chat memory and tools.
 * Typically instantiated once and reused for the lifetime of a scoping session.
 *
 * <p>Replaces the earlier {@code UzzoScopeAgentProvider} and is the primary
 * entry point for obtaining a usable scope agent.
 */
public class ScopeAgentService {

    private final UzzoScopeAgent agent;

    /**
     * Creates a new scope agent service.
     *
     * @param chatModel the LangChain4j chat model to use
     * @param askTools the tools for user interaction during clarification
     * @throws IOException if the system prompt resource cannot be loaded
     */
    public ScopeAgentService(final ChatModel chatModel, final AskTools askTools) throws IOException {
        this.agent = AiServices.builder(UzzoScopeAgent.class)
            .chatModel(chatModel)
            .chatMemory(MessageWindowChatMemory.withMaxMessages(50))
            .tools(askTools)
            .build();
    }

    /**
     * Processes a user message in the scoping dialogue.
     *
     * <p>Delegates to the underlying {@link UzzoScopeAgent} after validation.
     *
     * @param userMessage the developer's message (first call: project prompt)
     * @return the agent's response
     * @throws IllegalArgumentException if userMessage is null, empty, or whitespace-only
     */
    public String scope(final String userMessage) {
        return agent.scope(userMessage);
    }
}
