---
title: Home
layout: home
nav_order: 1
---

# Uzzo Workflow
{: .fs-9 }

AI-based software development for Product Owners and Product Engineers.
{: .fs-6 .fw-300 }

[Get started](getting-started){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }
[View on GitHub](https://github.com/stefanofornari/uzzo-workflow){: .btn .fs-5 .mb-4 .mb-md-0 }

---

Uzzo consists of three integrated pillars:

1. **AI-Based Workflow**: an agentic LLM framework for structured scope
   definition, gap analysis, user story writing, and coding. It helps product
   owners clarify requirements through interactive dialogue, surfacing missing
   details, ambiguities, and architectural gaps.
2. **Uzz Knowledge Base**: a structured Markdown repository holding all project
   information (scope, library references, project metadata, workflow skills,
   and user stories). It is the single source of truth shared across all
   workflow phases.
3. **Skills & Agents**: reusable Uzzo skills and LangChain4j agents that operate
   on the knowledge base to automate scoping, story writing, code generation,
   and KB management.

## Features

- **Interactive scope clarification**: structured dialogue to gather missing requirements
- **Gap analysis**: detects platform assumptions, architectural constraints, and ambiguous specifications
- **Boundary definition**: partitions capabilities into In-Scope and Out-of-Scope
- **Structured output**: standardized scope documents in Markdown
- **Extensible tools**: a pluggable `AskTools` interface for CLI, GUI, or web interaction

## Related projects

- [Viber](https://github.com/stefanofornari/viber): a GUI that implements Uzzo Workflow development
- [LangChain4j](https://github.com/langchain4j/langchain4j): LLM orchestration
