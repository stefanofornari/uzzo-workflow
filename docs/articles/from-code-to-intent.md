# From Code to Intent: A User Story-Driven Workflow for AI Software Development

For most of software engineering's history, the bottleneck was never the idea. It was the translation. A product owner had a vision, an engineer had a keyboard, and between the two sat months of specifications, misunderstandings, and rework. Then the engineer wrote code, and the code became the truth.

That arrangement is beginning to change as writing code is becoming less central to the work of creating it. As AI systems become increasingly capable of implementing software from natural-language instructions, the value moves up the stack: toward deciding *what* should exist, *why* it matters, and under which constraints it should operate. The craft of development does not disappear, its center of gravity shifts.

In that future, the people who shape and build software may not fit the traditional definition of a developer. Product owners will play a more direct role in turning needs into explicit requirements and priorities. Product engineers will connect those intentions to technical decisions, architecture, and implementation. AI agents will increasingly handle parts of the execution. What will set people apart is who can define the right outcome, make trade-offs explicit, and keep the work aligned with the original intent.

Uzzo Workflow is built for that shift.

## The New Bottleneck Is Understanding

When an AI agent can produce working code from a prompt, typing speed is no longer the main constraint. Shared understanding is. Teams that move effectively will be the ones that can take a rough idea, surface hidden assumptions, agree on boundaries, and keep everyone aligned while implementation takes shape.

User stories are pivotal to this process because they express the value the software is intended to deliver. They help connect a user's need to an outcome, and provide a practical unit for discussing scope, priorities, and acceptance. But user stories are only useful when they are grounded in a shared understanding of the product, its domains, its constraints, and the decisions already made.

That is why AI-assisted development needs more than prompts and generated code. It needs a persistent, shared representation of what the software is supposed to do—one that people can understand, review, and evolve, and that AI agents can use throughout the development process.

Uzzo Workflow is an AI-based software development workflow for product owners and product engineers. It brings together three integrated pillars:

1. **AI-Based Workflow** — An agentic framework for structured scope definition, gap analysis, user story writing, and coding.
2. **The Uzzo Knowledge Base** — A structured Markdown repository that holds the project's scope, domains, library references, metadata, skills, and stories. It provides a shared source of truth across the workflow.
3. **Skills and Agents** — Reusable workflow skills and [LangChain4j](https://docs.langchain4j.dev/) agents that operate on the knowledge base to support scoping, story writing, code generation, and knowledge-base management.

Together, these pillars replace disconnected documents and hand-offs with a continuously maintained context that is both human-readable and machine-readable.

## The Knowledge Base: Intent That Persists

Most projects scatter their context across tickets, documents, chat messages, and individual memories. Decisions become difficult to trace, and the code often becomes the only artifact that reliably survives. Uzzo takes a different approach: the knowledge base is a durable representation of the product's intent, and code is one of the things that can be produced from it.

The knowledge base is the shared context that connects the workflow's actions. Scope records the capabilities and boundaries of the system. User stories express the value to be delivered in manageable increments. Supporting library references, metadata, and reusable skills provide further context to both people and agents. As the understanding of a project changes, the knowledge base can evolve with it.

The KB is organized into four top-level directories—`library/`, `project/`, `skills/`, and `stories/`—with conventions designed to keep it navigable and consistent: no root files, nested categorization, full relative paths, and a `### Keywords` section on every article. User stories follow a consistent template and are tracked in an `index.md`, while each domain carries a narrative `scope.md`.

Because the content is plain Markdown, it can be reviewed, versioned, diffed, and consumed by a wide range of tools. The knowledge base is therefore not tied to a single interface or runtime. It can remain useful as the surrounding tools evolve.

That content is also shipped as a lean artifact, `uzzo-workflow-kb`, with no compile or runtime dependencies. A product engineer, a NetBeans plugin, or a CI pipeline can initialize a knowledge base from a known resource such as `/uzzo/skills/kb/SKILL.md` without bringing in the agent stack. The content evolves with the workflow, not with each consumer.

## Keeping Control While Moving Fast

The real risk of AI-assisted development is speed in the wrong direction: a model can produce something plausible without understanding the needs, constraints, or trade-offs behind a request.

Uzzo Workflow addresses this through its **scope** action. Rather than jumping directly from a prompt to code, `UzzoScopeAgent` takes a free-text project description and helps perform the work a good product and engineering team does at the start of a project:

- Identify missing requirements, platform assumptions, and architectural gaps.
- Ask targeted clarifying questions through a pluggable `AskTools` interface.
- Partition capabilities into **In-Scope** and **Out-of-Scope** boundaries.
- Produce a structured `scope.md` document that is updated iteratively as clarification progresses.

The conversation is the control mechanism. Each answer narrows the space of possible interpretations; each boundary is recorded. When the analysis identifies multiple architectural domains, such as backend, frontend, storage, analysis, or infrastructure, each can be scoped separately, with its own domain-specific `scope.md`. This allows teams to work in parallel without losing the shared picture.

The result is a durable, versionable document that humans and agents can both read and build on as the work progresses. From there, the **story** action derives INVEST-compliant user stories and the **code** action implements them, all against the shared knowledge base.

## Extensible by Design

Uzzo Workflow does not assume a single user interface. The `AskTools` interface—`askQuestion`, `askSingleChoice`, `askMultipleChoice`, and `askConfirmation`—lets the same agent run in a CLI, a web app, a GUI, or a test harness. Platform-specific interaction is injected rather than hard-coded, so the workflow logic remains portable. A `Safe` utility handles input validation, and the scope document output is standardized Markdown.

No single tool or interface will own software development, so product owners and product engineers should be able to work where they already work, while different tools read from and write to the same knowledge base.

## Actions, not a fixed sequence

Uzzo Workflow provides three core actions - **scope**, **story**, and **code** - that teams move between fluidly rather than as a waterfall. A gap discovered during implementation should send the team back to scoping; a story refined during development should update the shared understanding. Scope, stories, and code are connected parts of an iterative process—not isolated hand-offs between stages.

The actions share the same knowledge base and work within the same Agile iteration cycle, so product intent can be progressively clarified, expressed as user stories, and translated into implementation while remaining traceable throughout the process. See [The Workflow](../workflow) for the full picture.

## Uzzo Workflow in Practice

Uzzo Workflow is open source under the Apache License 2.0. See the project READMEs for build and usage instructions.

To explore or integrate Uzzo Workflow into a development lifecycle, the following open-source repository artifacts are available:

- [**uzzo-workflow**](https://github.com/stefanofornari/uzzo-workflow): The core mono-repo containing the agentic framework, domain logic, and knowledge-base structures.
  - [**uzzo-workflow-kb**](https://github.com/stefanofornari/uzzo-workflow/tree/main/uzzo-workflow-kb): A lightweight, standalone artifact containing the core skills, templates, and conventions for the Uzzo Knowledge Base. It has no compile or runtime dependencies, making it easy to embed into an editor or workflow tool.
  - [**uzzo-workflow**](https://github.com/stefanofornari/uzzo-workflow/tree/main/uzzo-workflow): The primary Java engine module, containing the agents, LangChain4j integrations, scoping logic, and execution runtime.
- [**nb-uzzo-workflow**](https://github.com/stefanofornari/nb-uzzo-workflow): The Apache NetBeans plugin integration for Uzzo Workflow. It brings interactive scoping, knowledge-base navigation, and agentic assistant features directly into the NetBeans IDE.


