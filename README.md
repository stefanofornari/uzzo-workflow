# Uzzo Workflow

Uzzo is an AI-based software development workflow for Product Owners and Product
Engineers. It consists of three integrated pillars:

1. **AI-Based Workflow** - An LLM agentic framework for structured scope
   definition, gap analysis, user story writing, and coding.
2. **Uzz Knowledge Base** - A structured Markdown repository that organizes
   all project information and serves as the single source of truth.
3. **Skills & Agents** - Reusable Uzzo skills and LangChain4j agents that
   operate on the knowledge base to automate workflow tasks.

## Modules

- `uzzo-workflow-parent` - parent POM providing common build configuration,
  dependency management, and Maven Central publishing setup.
- `uzzo-workflow` - agent code, scope analysis, and the LangChain4j-based
  workflow. See [uzzo-workflow/README.md](uzzo-workflow/README.md).
- `uzzo-workflow-kb` - canonical Uzzo knowledge base content (skills,
  standards, seed articles) shipped as a lean classpath artifact. See
  [uzzo-workflow-kb/README.md](uzzo-workflow-kb/README.md).

## Requirements

- Java 21+
- Maven 3.9+

## Building

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Publishing to Maven Central

Publish both `uzzo-workflow` and `uzzo-workflow-kb`:

```bash
mvn release:prepare release:perform
```

## License

Apache License 2.0 - see [LICENSE](LICENSE) for details.

## Related Projects

- [Viber](https://github.com/stefanofornari/viber) - A GUI that implements Uzzo Workflow Development
