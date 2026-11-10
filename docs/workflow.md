---
title: The Workflow
layout: default
nav_order: 2
has_children: true
---

# The Workflow

Uzzo Workflow is a complete software development workflow built around three
core actions, inspired by and supporting Agile principles.

| Action | Description |
|--------|-------------|
| [Scope](workflow/scoping) | Interactive gap analysis and boundary definition via `UzzoScopeAgent` |
| [Story](workflow/user-stories) | Generation of INVEST-compliant user stories from scope via `UzzoStoryAgent` |
| [Code](workflow/coding) | Implementation of user stories from specification to tested code via `UzzoCodeAgent` |

## Agile iteration, not a fixed sequence

The actions are **not a strict linear sequence**. Product owners and engineers
move fluidly between them:

- Scope a feature, write stories, start coding
- Discover a gap while coding, then return to scoping
- Refine a story during implementation, then update scope or stories
- Split a story, then re-scope the new pieces

The workflow favors **continuous clarification** over big upfront design. Each
action feeds the others, and the agents keep context through the shared
knowledge base.
