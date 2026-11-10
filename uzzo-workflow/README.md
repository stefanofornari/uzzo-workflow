# Uzzo Workflow

Uzzo is an AI-based software development workflow for Product Owners and Product Engineers.

## Workflow actions

Uzzo Workflow provides three core actions, inspired by and supporting Agile
principles:

| Action | Description |
|--------|-------------|
| **Scope** | Interactive gap analysis and boundary definition via `UzzoScopeAgent` |
| **Story** | Generation of INVEST-compliant user stories from scope via `UzzoStoryAgent` |
| **Code** | Implementation of user stories from specification to tested code via `UzzoCodeAgent` |

### Agile iteration, not a fixed sequence

These actions are **not a strict linear sequence**. In true Agile fashion,
product owners and engineers move fluidly between them:

- Scope a feature, write stories, start coding
- Discover a gap while coding → return to scoping
- Refine a story during implementation → update scope or stories
- Split a story → re-scope the new pieces

The workflow favors **continuous clarification** over big upfront design. Each
action feeds the others, and agents maintain context through the shared
knowledge base.

## Uzzo Knowledge Base

The Uzzo Knowledge Base (KB) is a structured Markdown repository that serves as
the single source of truth for the workflow. All actions read from and write to
the KB, keeping scoping, story writing, and coding consistent. The KB content is
shipped separately by the `uzzo-workflow-kb` artifact.

### KB Structure

The KB consists of Markdown articles organized into four primary top-level
directories:

| Directory | Purpose |
|-----------|---------|
| `library/` | General reference: development practices, language guides, frameworks, standard definitions |
| `project/` | Project-specific info: name, version, summary, team, timeline, repos, architecture |
| `skills/` | Uzzo workflow skills (reusable agent instructions) |
| `stories/` | User stories (one per file), organized by domain/feature |

#### Key Rules

- **No root files**: All articles must reside within the four directories above
- **Subdirectories**: Categorize articles into nested folders as appropriate
- **Full relative paths**: Reference articles by complete path
  (e.g., `stories/authentication/US101-dark-mode.md`)
- **Keywords section**: Every article MUST end with a `### Keywords` section
  containing search/categorization tags

### User Stories in the KB

Each file in `stories/` contains exactly **one** user story, following the
[story template](https://github.com/stefanofornari/uzzo-workflow/blob/main/src/main/uzz/skills/kb/references/user_story.md).
Stories are organized into subdirectories by domain (e.g., `stories/backend/`,
`stories/frontend/`). The file `stories/index.md` contains a table summarizing
all user stories (id and title) and their status of development (`TODO`, `WIP`,
`DONE`).

The overall scope of a project or domain is summarized in a `scope.md` document
under `stories/` or the domain's folder. Unlike `index.md`, `scope.md` is a
descriptive narrative of what the project or domain wants to achieve, providing
enough context to understand the domain without opening each story file and
capturing constraints and main design decisions.

## Actions

### Scope

`UzzoScopeAgent` / `ScopeAgentService`:

- Accepts free-text prompts (rough user stories or a rough description of a
  feature or goal)
- Identifies missing requirements, platform assumptions, and architectural gaps
- Asks targeted clarifying questions through the `AskTools` interface
  (LangChain4j tools)
- Produces a **scope.md** document with In-Scope/Out-of-Scope boundaries,
  updated iteratively as clarification progresses

When the analysis identifies multiple architectural domains (e.g., backend,
frontend, storage, analysis, infrastructure), each domain can be scoped
separately with a domain-specific `scope.md`.

### Story

`UzzoStoryAgent` (see `UzzoAgentBuilder.StoryAgentBuilder()`), backed by the
`uzz/skills/story/SKILL.md` skill, generates and refines INVEST-compliant user
stories from scope documents.

### Code

`UzzoCodeAgent` (see `UzzoAgentBuilder.CodeAgentBuilder()`), backed by the
`uzz/skills/code/SKILL.md` skill, implements a user story from specification to
tested code using the project context in the knowledge base.

## Features

- **Interactive scope clarification** - structured dialogue to gather missing
  requirements
- **Gap analysis** - detects platform assumptions, architectural constraints,
  and ambiguous specifications
- **Boundary definition** - partitions capabilities into In-Scope and
  Out-of-Scope categories
- **Story generation** - derives INVEST-compliant stories from scope
- **Story implementation** - drives implementation from specification to tested
  code
- **Structured output** - standardized Markdown artifacts in the knowledge base
- **Extensible tools** - pluggable `AskTools` interface for platform-specific
  user interaction (CLI, GUI, web)

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  User Prompt    │────▶│  Uzzo* Agents    │────▶│  AskTools       │
│  / context      │     │  (AI Services)   │     │  (interaction)  │
└─────────────────┘     └──────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌──────────────────┐
                        │  KB artifacts    │
                        │  (Markdown)      │
                        └──────────────────┘
```

### Core Components

| Component | Description |
|-----------|-------------|
| `UzzoScopeAgent` | LangChain4j AI service interface for scope analysis |
| `UzzoStoryAgent` | LangChain4j AI service interface for user-story generation |
| `UzzoCodeAgent` | LangChain4j AI service interface for story implementation |
| `UzzoAgentBuilder` | Builders configuring agents with skills, chat memory, and tools |
| `ScopeAgentService` | Provider that builds/configures the scope agent with chat memory and tools |
| `AskTools` | Interface for user interaction (questions, choices, confirmations) |
| `Safe` | Utility for input validation |

## Requirements

- Java 21+
- Maven 3.9+
- LangChain4j 1.21.0+

## Building

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Usage

### Basic Setup (scope)

```java
// Configure your LLM provider
ChatModel chatModel = ... // e.g., OpenAI, Anthropic, Ollama

// Implement AskTools for your platform (CLI, web, GUI)
AskTools tools = new MyAskToolsImplementation();

// Create the scope service
ScopeAgentService agent = new ScopeAgentService(chatModel, tools);
```

### Running Scope Analysis

```java
String prompt = """
    Build a web application for e-commerce using Node.js
    on AWS cloud platform with JWT authentication and Stripe payments.
    Architecture: microservices with REST API.
    """;

String response = agent.scope(prompt);
// Agent will ask clarifying questions if gaps are detected
// Returns structured scope document when complete
```

### Story and Code actions

Use `UzzoAgentBuilder.StoryAgentBuilder()` and
`UzzoAgentBuilder.CodeAgentBuilder()` to configure `UzzoStoryAgent` and
`UzzoCodeAgent` with your chat model, memory, and tools.

### Implementing AskTools

```java
public class CliAskTools implements AskTools {

    @Override
    public String askQuestion(String question) {
        System.out.print(question + " ");
        return new Scanner(System.in).nextLine();
    }

    @Override
    public String askSingleChoice(String question, List<String> options) {
        // Present options, read selection
    }

    @Override
    public List<String> askMultipleChoice(String question, List<String> options) {
        // Present options, read multiple selections
    }

    @Override
    public boolean askConfirmation(String question) {
        // Present yes/no, read confirmation
    }
}
```

## Scope document output

The scope action generates a structured Markdown document:

```markdown
# Project Name: E-Commerce Platform
**Version:** 1.0.0

## Overall Description
[High-level breakdown by functional domains]

## In-Scope Capabilities
* **User Management**:
  - [ ] JWT authentication
  - [ ] Profile management

## Out-of-Scope / Future Enhancements
* Mobile application
* Advanced analytics dashboard

## Technical Context & Infrastructure
* **Development Toolchain**: Maven + Node.js
* **Target Systems/Platforms**: Web, AWS
* **Project Reference Docs**:
  - Coding Standard: `coding_standard.md`
  - Development Framework: `development_framework.md`
```

## License

Apache License 2.0 - see [LICENSE](../LICENSE) for details.
