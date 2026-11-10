# Uzzo Workflow

Uzzo is an AI-based software development workflow for Product Owners and Product
Engineers. It combines an agentic workflow, a structured Markdown knowledge base,
and reusable skills/agents to move from intent to scoped, testable work.

## Pillars

1. **AI-based workflow** - three core actions: **scope** (gap analysis and
   boundary definition), **story** (INVEST user-story generation from scope),
   and **code** (implementation of stories), all backed by agents and skills.
2. **Uzzo Knowledge Base** - a shared Markdown repository (`library/`,
   `project/`, `skills/`, `stories/`) that keeps project context durable and
   reviewable.
3. **Skills & agents** - reusable workflow skills and LangChain4j agents that
   operate on the knowledge base.

## Modules

- `uzzo-workflow-parent` - parent POM with common build configuration,
  dependency management, and Maven Central publishing setup.
- `uzzo-workflow` - workflow agents/runtime and LangChain4j integration (scope,
  story, code actions). See [uzzo-workflow/README.md](uzzo-workflow/README.md).
- `uzzo-workflow-kb` - canonical Uzzo knowledge base content (workflow skills,
  standards, seed articles) packaged as a lean, dependency-free classpath
  artifact under `/uzz/`. See [uzzo-workflow-kb/README.md](uzzo-workflow-kb/README.md).

## Documentation

Project documentation is published at
<https://stefanofornari.github.io/uzzo-workflow/> (source in [`docs/`](docs/)),
including:

- [Getting started](https://stefanofornari.github.io/uzzo-workflow/getting-started) (Java API)
- [Installing skills](https://stefanofornari.github.io/uzzo-workflow/installing-skills) - install Uzzo skills from the `uzzo-workflow-kb` jar (Maven Central) into GitHub Copilot, Claude Code, or OpenCode
- [Knowledge base](https://stefanofornari.github.io/uzzo-workflow/knowledge-base) and [workflow](https://stefanofornari.github.io/uzzo-workflow/workflow)

## Requirements

- Java 21+
- Maven 3.9+

## Build and test

```bash
mvn clean compile
mvn test
```

## Publishing to Maven Central

Publishes `uzzo-workflow` and `uzzo-workflow-kb`:

```bash
mvn release:prepare release:perform
```

## License

Apache License 2.0 - see [LICENSE](LICENSE) for details.

## Related projects

- [nb-uzzo-workflow](https://github.com/stefanofornari/nb-uzzo-workflow) - NetBeans plugin integration
- [Viber](https://github.com/stefanofornari/viber) - GUI implementing Uzzo Workflow development
