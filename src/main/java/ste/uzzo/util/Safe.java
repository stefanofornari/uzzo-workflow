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
package ste.uzzo.util;

/**
 * Utility class for safe null checks and argument validation.
 */
public class Safe {

    /**
     * Throws IllegalArgumentException if the string is null, empty, or whitespace-only.
     * @param str the string to check
     * @param paramName the parameter name for the error message
     * @throws IllegalArgumentException if str is null, empty, or whitespace-only
     */
    public static void requireNonBlank(final String str, final String paramName) {
        if (str == null || str.isBlank()) {
            throw new IllegalArgumentException(paramName + " cannot be null or empty");
        }
    }

    /**
     * Throws IllegalArgumentException if the object is null.
     * @param obj the object to check
     * @param paramName the parameter name for the error message
     * @throws IllegalArgumentException if obj is null
     */
    public static void requireNonNull(final Object obj, final String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException(paramName + " cannot be null");
        }
    }
}
