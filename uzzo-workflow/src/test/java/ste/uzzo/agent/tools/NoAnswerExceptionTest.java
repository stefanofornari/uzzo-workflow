package ste.uzzo.agent.tools;

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

import dev.langchain4j.exception.ToolErrorVisibleToLlm;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

/**
 *
 */
public class NoAnswerExceptionTest {

    @Test
    public void NoAnswerException_is_a_RuntimeException_implementing_ToolErrorVisibleToLlm() {
        then(new NoAnswerException())
            .isInstanceOf(RuntimeException.class).isInstanceOf(ToolErrorVisibleToLlm.class)
            .hasMessage("<NO ANSWER PROVIDED>");
    }

    @Test
    public void default_constructor_getMessage_and_messageForLlm_return_default() {
        NoAnswerException exception = new NoAnswerException();
        then(exception.getMessage()).isEqualTo(NoAnswerException.DEFAULT_NO_ANSWER);
        then(exception.messageForLlm()).isEqualTo(exception.getMessage());
    }

    @Test
    public void constructor_with_null_message_uses_default() {
        NoAnswerException exception = new NoAnswerException(null);
        then(exception.getMessage()).isEqualTo(NoAnswerException.DEFAULT_NO_ANSWER);
        then(exception.messageForLlm()).isEqualTo(exception.getMessage());
    }

    @Test
    public void constructor_with_custom_message_uses_provided_message() {
        NoAnswerException exception = new NoAnswerException("custom message");
        then(exception.getMessage()).isEqualTo("custom message");
        then(exception.messageForLlm()).isEqualTo(exception.getMessage());
    }
}
