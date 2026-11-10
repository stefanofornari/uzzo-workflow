---
title: Scope
layout: default
parent: The Workflow
nav_order: 1
---

# Scope action

The **scope** action performs interactive gap analysis and boundary definition
via `UzzoScopeAgent` (and `ScopeAgentService`). The agent:

- accepts free-text prompts: rough user stories, or a rough description of a
  feature or goal
- identifies missing requirements, platform assumptions, and architectural gaps
- asks targeted clarifying questions through the `AskTools` interface
  (LangChain4j tools)
- produces a **scope.md** document with In-Scope / Out-of-Scope boundaries,
  **updated iteratively** as clarification progresses

When the analysis identifies multiple architectural domains (backend, frontend,
storage, analysis, infrastructure, ...), each domain can be scoped separately.
The agent generates a **domain-specific scope.md** for each, so teams can work
in parallel with clear boundaries.

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

| Component | Description |
|-----------|-------------|
| `UzzoScopeAgent` | LangChain4j AI service interface defining the scope analysis contract |
| `ScopeAgentService` | Concrete provider that builds and configures the agent with chat memory and tools |
| `AskTools` | Interface for user interaction (questions, choices, confirmations) |
| `Safe` | Utility for input validation |

## Scope document output

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
```
