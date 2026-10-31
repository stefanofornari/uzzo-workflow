# Project Name: Uzzo Workflow
**Version:** 0.0.0-SNAPSHOT

## 📝 Overall Description
The Scope Agent is a conversational agent that executes the scope-definition-reviewer skill to autonomously scope functionalities of a project from a user prompt. Operating within the Viber `Viber`-`Actor` framework, it engages in an interactive clarifying dialogue with the user, defines implementation boundaries, and compiles a standardized scope document.

Functional domains:
- **Prompt Analysis & Gap Detection**: Receive and analyze the initial user prompt to identify missing or ambiguous requirements
- **Interrogative Clarification Engine**: Generate and manage targeted clarifying questions in properly paced clusters
- **Boundary Definition & Extraction**: Partition all discussed items into mutually exclusive In-Scope and Out-of-Scope groups
- **Scope Compilation**: Generate a structured scope document using the exact template defined in the skill
- **Persistence**: Save or update the compiled scope document to a target file
- **Distribution**: Publish the canonical knowledge base content (skills, standards, seed articles) as a lean, dependency-free artifact for other tools
## 🎯 In-Scope Capabilities
* **Prompt Analysis & Gap Detection**:
  - [ ] Accept a user prompt describing a project, feature, or user story
  - [ ] Analyze prompt for gaps, ambiguities, or missing details
  - [ ] Identify unresolved requirements, platform assumptions, and architectural constraints

* **Interrogative Clarification Engine**:
  - [ ] Generate targeted clarifying questions to collect missing details
  - [ ] Pace questions appropriately (one by one or small clusters, maximum 3 questions at a time)
  - [ ] Process user answers and update project understanding iteratively
  - [ ] Determine when sufficient clarification has been gathered to proceed to boundary definition

* **Boundary Definition & Extraction**:
  - [ ] Partition all discussed items into In-Scope Capabilities
  - [ ] Partition all discussed items into Out-of-Scope / Future Enhancements
  - [ ] Enforce Scope Guarding: treat anything not explicitly listed as Out-of-Scope by default

* **Scope Compilation**:
  - [ ] Generate structured scope document using the exact template from `uzz/skills/scope/SKILL.md`
  - [ ] Populate Project Name, Version, and Overall Description fields
  - [ ] Populate In-Scope Capabilities with checkbox items organized by domain
  - [ ] Populate Out-of-Scope / Future Enhancements with explicit boundary items
  - [ ] Populate Technical Context & Infrastructure section
  - [ ] Enforce No Speculation policy: ask the user instead of assuming unknown technical stacks, platforms, or reference paths

* **Persistence**:
  - [ ] Write scope document to a target markdown file
  - [ ] Update existing scope files in place
  - [ ] Create new scope files when target does not exist

* **Distribution**:
  - [ ] Publish `src/main/uzz` as the dependency-free `uzzo-workflow-kb` artifact (multi-module build), content under the `uzz/` prefix
## 🚫 Out-of-Scope / Future Enhancements
- Implementing the actual features or capabilities described in the generated scope document
- Providing technical implementation guidance, architectural recommendations, or code generation
- Assuming technical stacks, platforms, project reference paths, or any unknown value (No Speculation violation)
- Modifying source code of the target project
- Integration with version control systems (Git, etc.)
- Multi-session scope refinement or scope versioning
- Scope document translation or localization
- Automated scope validation against existing codebases
- Scope merging or diffing across multiple projects
- Integration with project management tools (Jira, etc.)

## 🛠️ Technical Context & Infrastructure
- **Development Toolchain**: Java, JavaFX, Maven, JUnit 5, TestFX, AssertJ, TDD-oriented workflow
- **Agent Capability Framework**: LangChain4j — provides AI Services, ChatMemory, and Tools (function calling)
- **Target Systems/Platforms**: ViberFX desktop application, ViberCLI terminal interface, embeddable host applications
- **Skill Source**: `uzz/skills/scope/SKILL.md`
- **Output Template**: Markdown, as defined in the Scope Definition Reviewer skill
- **Project Reference Docs**:
  - Coding Standard: `uzz/project/coding_standard.md`
  - Development Framework: `uzz/project/development_framework.md`
  - LangChain4j Overview: `uzz/library/langchain4j/overview.md`
  - LangChain4j AI Services Guide: `uzz/library/langchain4j/ai-services-scope-agent.md`
  - LangChain4j Tutorials: https://docs.langchain4j.dev/tutorials/ai-services
