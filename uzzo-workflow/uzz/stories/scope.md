# Project Name: Uzzo Workflow
**Version:** 0.0.2-SNAPSHOT

## Overall Description

Uzzo Workflow is a Java library that provides the LangChain4j-based agents behind the three actions of the Uzzo workflow: **scope**, **story** and **code**. Each agent is a LangChain4j AI Service interface whose system prompt is a skill (`SKILL.md`) taken from the Uzzo Knowledge Base shipped by the `uzzo-workflow-kb` artifact. The library is deliberately thin: it defines the agent contracts, loads the skills as system prompts and defines the contract used by agents to talk to the user. The chat model, chat memory, tool implementations and user interface are supplied by the host application.

Agents answer with a `TokenStream`, so hosts can render responses incrementally. Every agent entry point rejects null, empty or whitespace-only input before any model call is made.

Functional domains:
- **Agent Contracts**: the `UzzoScopeAgent`, `UzzoStoryAgent` and `UzzoCodeAgent` AI Service interfaces and their input validation
- **Skill-Based System Prompts**: loading a skill from the classpath as the agent's system message, and the builders that wire each agent to its skill
- **User Interaction Tools**: the `AskTools` contract that lets an agent ask the user questions, and the way a missing answer is reported back to the model

## In-Scope Capabilities

* **Agent Contracts**:
  - [x] Scope agent (`UzzoScopeAgent.scope`): accepts a free-text prompt and continues the clarification dialogue over the conversation history
  - [x] Story agent (`UzzoStoryAgent.stories`): accepts a request plus scope/persona context to generate or update user stories
  - [x] Code agent (`UzzoCodeAgent.implement`): accepts a request containing the user story to implement
  - [x] Return the agent's reply as a LangChain4j `TokenStream`
  - [x] Reject null, empty and whitespace-only input with an `IllegalArgumentException` before the model is called (via `Safe.requireNonBlank`)

* **Skill-Based System Prompts**:
  - [x] Load a skill from the classpath (e.g. `uzz/skills/scope/SKILL.md`) and use it as the agent's system message
  - [x] Strip the YAML frontmatter from the skill so only the skill body reaches the model
  - [x] Fail with a clear error when the skill resource is missing or unreadable
  - [x] Provide `UzzoAgentBuilder` with one builder per agent (`ScopeAgentBuilder`, `StoryAgentBuilder`, `CodeAgentBuilder`), each returning an `AiServices` already bound to its skill, for the host to complete with chat model, memory and tools
  - [x] Map each agent to its skill: scope → `uzz/skills/scope/SKILL.md`, story → `uzz/skills/story/SKILL.md`, code → `uzz/skills/code/SKILL.md`

* **User Interaction Tools**:
  - [x] Define the `AskTools` contract exposing `AskQuestion`, `AskSingleChoice`, `AskMultipleChoice` and `AskConfirmation` as LangChain4j tools
  - [x] Tell the model, in the tool descriptions, that `<NO ANSWER PROVIDED>` is returned when the user has no answer
  - [x] Provide `NoAnswerException`, visible to the model, whose message defaults to `<NO ANSWER PROVIDED>` and can be customized

## Out-of-Scope / Future Enhancements

* Concrete `AskTools` implementations for CLI, GUI or web: the host application provides them
* Choosing, configuring or authenticating the LLM provider, and chat memory configuration: the host provides the chat model and memory
* Writing, saving or updating `scope.md`, user story files, `index.md` or code on disk: the agents only produce the conversation; file operations are done by tools provided by the host
* A concrete provider class that wires model, memory and tools for the host (such as the former `ScopeAgentService`): hosts build agents with `UzzoAgentBuilder`
* The Knowledge Base content (skills, templates, standards): it is owned and shipped by the `uzzo-workflow-kb` module
* Authoring or changing the skill texts themselves
* A non-streaming (`String`) response API
* Any command line or `main` entry point
* Multi-session scope refinement or scope versioning
* Integration with version control systems or project management tools

## Technical Context & Infrastructure

* **Development Toolchain**: Java 21, Maven (multi-module build with `uzzo-workflow-kb` and `uzzo-workflow`), TDD with JUnit 5, AssertJ (BDD style) and xtest
* **Agent Capability Framework**: LangChain4j 1.21.0 (AI Services, `TokenStream`, tools / function calling, `ToolErrorVisibleToLlm`)
* **Key Dependencies**: `uzzo-workflow-kb` (skills as classpath resources), `uzzo-llm` (`ste.llm.tools.Tool`), `lazyjava-core` (`Safe`), `xtest` (`DummyStreamingChatModel`, `DummyChatModelListener`) for tests
* **Target Systems/Platforms**: any JVM host application (e.g. ViberFX desktop, ViberCLI terminal) that supplies a streaming chat model and `AskTools`
* **Project Reference Docs**:
  - Project Overview: `uzz/project/overview.md`
  - Coding Standard: `uzz/project/coding_standard.md`
  - Development Framework: `uzz/project/development_framework.md`

### Keywords
- scope
- requirements
- boundaries
- in-scope
- out-of-scope
- agents
- langchain4j
- skills
- ask-tools
