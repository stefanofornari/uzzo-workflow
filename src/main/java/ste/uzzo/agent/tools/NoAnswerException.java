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

package ste.uzzo.agent.tools;

import dev.langchain4j.exception.ToolErrorVisibleToLlm;

/**
 * Exception thrown when a tool execution did not produce an answer.
 * This exception is visible to the LLM via the {@link ToolErrorVisibleToLlm} interface.
 * <p>
 * The {@link #getMessage()} method returns the same value as {@link #messageForLlm()},
 * which is either the provided message or the default "{@value #DEFAULT_NO_ANSWER}".
 */
public class NoAnswerException extends RuntimeException implements ToolErrorVisibleToLlm {

    /**
     * Default message used when no message is provided.
     */
    public final static String DEFAULT_NO_ANSWER = "<NO ANSWER PROVIDED>";

    /**
     * Creates a new exception with the default message.
     */
    public NoAnswerException() {
        this(null);
    }

    /**
     * Creates a new exception with the specified message.
     * If the message is {@code null}, the default message is used.
     *
     * @param message the detail message, or {@code null} to use the default
     */
    public NoAnswerException(String message) {
        super(message != null ? message : DEFAULT_NO_ANSWER);
    }

    @Override
    public String messageForLlm() {
        return getMessage();
    }

}
