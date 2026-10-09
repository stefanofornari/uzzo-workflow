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

import java.util.List;

import dev.langchain4j.agent.tool.Tool;

/**
 * Tools for user interaction during scope clarification dialogue.
 *
 * The Scope Agent calls these tools to gather information from the developer.
 * Implementations are platform-specific (CLI, GUI, etc.) but follow this contract.
 *
 * Developers provide their own implementations based on deployment context.
 */
public interface AskTools {

    /**
     * Ask the user an open-ended question.
     *
     * @param question the question to ask
     * @return the user's free-text response
     */
    @Tool(name = "AskQuestion", value = "Ask the user an open-ended question and return their response")
    String askQuestion(String question);

    /**
     * Ask the user to select one option from a list.
     *
     * @param question the question to ask
     * @param options list of options to choose from (e.g., ["Web", "Mobile", "Desktop"])
     * @return the selected option
     */
    @Tool(name = "AskSingleChoice", value = "Ask the user to select one option from a list")
    String askSingleChoice(String question, List<String> options);

    /**
     * Ask the user to select multiple options from a list.
     *
     * @param question the question to ask
     * @param options list of options to choose from
     * @return list of selected options
     */
    @Tool(name = "AskMultipleChoice", value = "Ask the user to select multiple options from a list")
    List<String> askMultipleChoice(String question, List<String> options);

    /**
     * Ask the user to confirm a statement or decision.
     *
     * @param question the confirmation question
     * @return true if user confirms, false otherwise
     */
    @Tool(name = "AskConfirmation", value = "Ask the user to confirm a decision (yes/no)")
    boolean askConfirmation(String question);
}
