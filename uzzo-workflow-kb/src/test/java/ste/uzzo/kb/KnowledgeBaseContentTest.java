package ste.uzzo.kb;

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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.BDDAssertions.then;
import org.junit.jupiter.api.Test;

/**
 * US-000014 - the knowledge base content is available on the classpath.
 */
public class KnowledgeBaseContentTest {

    @Test
    public void content_is_available_as_classpath_resources() {
        then(getClass().getResource("/uzz/skills/kb/SKILL.md")).isNotNull();
        then(getClass().getResource("/uzz/skills/kb/references/user_story.md")).isNotNull();
    }

    @Test
    public void every_shipped_file_is_on_the_classpath_without_os_noise() throws IOException {
        Path root = Paths.get("..", "src", "main", "uzz");

        List<String> sources;
        try (Stream<Path> files = Files.walk(root)) {
            sources = files.filter(Files::isRegularFile)
                .map(p -> root.relativize(p).toString().replace('\\', '/'))
                .toList();
        }

        then(sources).isNotEmpty();
        for (String path : sources) {
            if (path.endsWith(".DS_Store")) {
                then(getClass().getResource("/uzz/" + path)).as(path).isNull();
            } else {
                then(getClass().getResource("/uzz/" + path)).as(path).isNotNull();
            }
        }
    }
}
