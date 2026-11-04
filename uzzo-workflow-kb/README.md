# Uzzo Workflow Knowledge Base Content

`uzzo-workflow-kb` is a lean artifact that ships the canonical Uzzo knowledge
base content (skills, standards, and seed articles) as classpath resources under
the `uzz/` prefix. It declares no compile or runtime dependencies, so consumers
can initialize a knowledge base programmatically without pulling in the agent
dependencies.

## Maven Dependency

```xml
<dependency>
    <groupId>com.github.stefanofornari</groupId>
    <artifactId>uzzo-workflow-kb</artifactId>
    <version>0.0.0-SNAPSHOT</version>
</dependency>
```

## Content Layout

All content is available as classpath resources under `/uzz/`:

- `uzz/skills/` - Uzzo workflow skills (reusable agent instructions)
- `uzz/skills/kb/` - knowledge base skill and references
- `uzz/skills/uzzo-init/` - initialization templates and profiles

## Usage

Resources can be read directly from the classpath, e.g.
`/uzz/skills/kb/SKILL.md`. Consumers locate content from a known resource and
list it through the location it resolves to (jar or directory).

## Building

```bash
mvn clean compile
```

## License

Apache License 2.0 - see [LICENSE](../LICENSE) for details.
