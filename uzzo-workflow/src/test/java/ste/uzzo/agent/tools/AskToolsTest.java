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

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dev.langchain4j.agent.tool.Tool;
import org.junit.jupiter.api.Test;

/**
 * US-000010 - the AskTools contract tells the model what happens when the user
 * has no answer.
 */
public class AskToolsTest {

    @Test
    void ask_tools_exposes_the_four_ask_tools() {
        then(tools()).extracting(Tool::name).containsExactlyInAnyOrder(
            AskTools.ASK_QUESTION,
            AskTools.ASK_SINGLE_CHOICE,
            AskTools.ASK_MULTIPLE_CHOICE,
            AskTools.ASK_CONFIRMATION
        );
    }

    @Test
    void every_tool_description_mentions_the_no_answer_marker() {
        for (final Tool tool: tools()) {
            then(String.join(" ", tool.value()))
                .as("description of " + tool.name())
                .contains(NoAnswerException.DEFAULT_NO_ANSWER);
        }
    }

    // --------------------------------------------------------- private methods

    private List<Tool> tools() {
        return Arrays.stream(AskTools.class.getDeclaredMethods())
            .map(method -> method.getAnnotation(Tool.class))
            .filter(Objects::nonNull)
            .toList();
    }
}
