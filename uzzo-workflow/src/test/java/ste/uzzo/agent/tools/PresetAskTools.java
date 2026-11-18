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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dev.langchain4j.agent.tool.Tool;
import ste.llm.tools.AbstractTool;

/**
 * An AskTools implementation for tests: it records what it is asked and answers
 * with preset values, or fails with the preset exception.
 */
class PresetAskTools extends AbstractTool implements AskTools {

    final List<String> calls = new ArrayList<>();
    String question;
    List<String> options;
    String singleChoice;
    List<String> multipleChoice;
    boolean confirmation;
    RuntimeException failure;

    PresetAskTools() throws IOException {
        super(new File(".").getAbsolutePath());
    }

    @Override
    @Tool(name = ASK_QUESTION, value = "Ask the user an open-ended question")
    public String askQuestion(final String question) {
        calls.add(ASK_QUESTION);
        return "";
    }

    @Override
    @Tool(name = ASK_SINGLE_CHOICE, value = "Ask the user to select one option")
    public String askSingleChoice(final String question, final List<String> options) {
        calls.add(ASK_SINGLE_CHOICE);
        this.question = question;
        this.options = options;
        if (failure != null) {
            throw failure;
        }
        return singleChoice;
    }

    @Override
    @Tool(name = ASK_MULTIPLE_CHOICE, value = "Ask the user to select multiple options")
    public List<String> askMultipleChoice(final String question, final List<String> options) {
        calls.add(ASK_MULTIPLE_CHOICE);
        this.question = question;
        this.options = options;
        if (failure != null) {
            throw failure;
        }
        return multipleChoice;
    }

    @Override
    @Tool(name = ASK_CONFIRMATION, value = "Ask the user to confirm")
    public boolean askConfirmation(final String question) {
        calls.add(ASK_CONFIRMATION);
        this.question = question;
        if (failure != null) {
            throw failure;
        }
        return confirmation;
    }
}
