---
title: The Workflow
layout: default
nav_order: 2
has_children: true
---

# The Three-Phase Workflow

Uzzo Workflow is a complete software development workflow made of three
interconnected phases, inspired by and supporting Agile principles.

| Phase | Status | Description |
|-------|--------|-------------|
| [Scoping](workflow/scoping) | Implemented | Interactive gap analysis and boundary definition via `UzzoScopeAgent` |
| [User Story Writing](workflow/user-stories) | Planned | AI-assisted story creation, refinement, and INVEST validation |
| Coding | Planned | Test-driven implementation guided by accepted stories and scope |

## Agile iteration, not waterfall

The phases are **not a strict linear sequence**. Product owners and engineers
move fluidly between them:

- Scope a feature, write stories, start coding
- Discover a gap while coding, then return to scoping
- Refine a story during implementation, then update the scope
- Split a story, then re-scope the new pieces

The workflow favors **continuous clarification** over big upfront design. Each
phase feeds the others, and the AI agents keep context across transitions.
