# Uzzo Workflow

Uzzo is an AI-based software development for Product Owners and Product Engineers.

## The Three-Phase Workflow

Uzzo Workflow is designed as a complete software development workflow consisting
of three interconnected phases, inspired by and supporting Agile development
principles:

| Phase | Status | Description |
|-------|--------|-------------|
| **Scoping** | ✅ Implemented | Interactive gap analysis and boundary definition via `UzzoScopeAgent` |
| **User Story Writing** | 🔄 Planned | AI-assisted story creation, refinement, and INVEST validation |
| **Coding** | 🔄 Planned | Test-driven implementation guided by accepted stories and scope |

### Agile Iteration, Not Waterfall Sequence

These phases are **not a strict linear sequence**. In true Agile fashion,
product owners and engineers will move fluidly between them:

- Scope a feature, write stories, start coding
- Discover a gap while coding → return to scoping
- Refine a story during implementation → update scope
- Split a story → re-scope the new pieces

The workflow embraces **continuous clarification** rather than big upfront
design. Each phase feeds the others, and the AI agents maintain context across
transitions.

## Uzzo Knowledge Base

The Uzzo Knowledge Base (KB) is a structured Markdown repository that serves as
the single source of truth for the entire workflow. All agents read from and
write to the KB, ensuring consistency across scoping, story writing, and coding
phases. The KB content is shipped separately by the `uzzo-workflow-kb` artifact.

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
template defined in [`library/references/user_story.md`](https://github.com/stefanofornari/uzzo-workflow/blob/main/src/main/uzz/skills/kb/references/user_story.md).
Stories are organized into subdirectories by domain (e.g., `stories/backend/`,
`stories/frontend/`). The file `stories/index.md` contains a table summarizing
all user stories (id and title) and their status of development (`TODO`, `WIP`, `DONE`).

The overall scope of a project or domain is summarized in a `scope.md` document
under `stories/` or the domain's folder. Unlike `index.md`, `scope.md` is a
descriptive narrative of what the project or domain want to achieve, providing
enough context to understand the domain without opening each story file and
providing information about the constraints or main design decition taken.

## Scoping Phase

The current release provides the **Scoping phase** via `UzzoScopeAgent` and
`ScopeAgentService`. This agent:

- Accepts free text project prompts, maybe rough user stories or just rough description
  of a feature or goal to achive
- Identifies missing requirements, platform assumptions, and architectural gaps
- Asks targeted clarifying questions through the `AskTools` interface (LangChain4j tools)
- Produces a **scope.md** document with In-Scope/Out-of-Scope boundaries
  that is **updated iteratively** as clarification progresses

When the analysis identifies multiple architectural domains (e.g., backend,
frontend, storage, analysis, infrastructure), each domain can be scoped
separately. The agent generates a **domain-specific scope.md** for each,
allowing teams to work on different areas in parallel while maintaining
clear boundaries.

Future releases will add agents for user story writing and coding assistance,
all sharing a common context and working within the same Agile iteration cycle.

## User Stories

User stories are a fundamental tool in Agile software development for capturing
requirements from the perspective of the end user.

### What Are User Stories?

A user story is a short, simple description of a feature told from the
perspective of the person who desires the new capability, usually a user or
customer of the system. The standard format follows:

> **As a [role], I want [goal/desire] so that [benefit/value].**

### Key Principles

| Principle | Description |
|-----------|-------------|
| **Independent** | Stories should be independent of each other to allow flexible prioritization |
| **Negotiable** | Details are negotiated through conversation, not fixed in a contract |
| **Valuable** | Each story must deliver value to the user or customer |
| **Estimable** | The team must be able to estimate the effort required |
| **Small** | Stories should be small enough to complete in one iteration |
| **Testable** | Clear acceptance criteria must exist to verify completion |

These principles are often remembered by the **INVEST** acronym.

### User Stories in Uzzo Workflow

Uzzo Workflow treats user stories as the primary input for the coding phase. The
agent. More details TBD.

## Features

- **Interactive Scope Clarification** - AI agent engages in structured
  dialogue to gather missing requirements
- **Gap Analysis** - Automatically detects platform assumptions,
  architectural constraints, and ambiguous specifications
- **Boundary Definition** - Partitions capabilities into In-Scope and
  Out-of-Scope categories
- **Structured Output** - Generates standardized scope documents in Markdown
  format
- **Extensible Tools** - Pluggable `AskTools` interface for platform-specific
  user interaction (CLI, GUI, web)

## Architecture

```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────────┐
│  User Prompt    │────▶│  UzzoScopeAgent  │────▶│  AskTools       │
│  (requirements) │     │  (AI Service)    │     │  (interaction)  │
└─────────────────┘     └──────────────────┘     └─────────────────┘
                               │
                               ▼
                        ┌──────────────────┐
                        │  Scope Document  │
                        │  (Markdown)      │
                        └──────────────────┘
```

### Core Components

| Component | Description |
|-----------|-------------|
| `UzzoScopeAgent` | LangChain4j AI service interface defining the scope analysis contract |
| `ScopeAgentService` | Concrete provider that builds and configures the agent with chat memory and tools |
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

### Basic Setup

```java
// Configure your LLM provider
ChatModel chatModel = ... // e.g., OpenAI, Anthropic, Ollama

// Implement AskTools for your platform (CLI, web, GUI)
AskTools tools = new MyAskToolsImplementation();

// Create the service
ScopeAgentService agent = new ScopeAgentService(chatModel, tools);
```

### Running Scope Analysis

```java
// Submit a project prompt
String prompt = """
    Build a web application for e-commerce using Node.js
    on AWS cloud platform with JWT authentication and Stripe payments.
    Architecture: microservices with REST API.
    """;

String response = agent.scope(prompt);
// Agent will ask clarifying questions if gaps are detected
// Returns structured scope document when complete
```

### Implementing AskTools

Create a platform-specific implementation:

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

## Scope Document Output

The agent generates a structured Markdown document:

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
