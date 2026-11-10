---
title: Code
layout: default
parent: The Workflow
nav_order: 3
---

# Code action

The **code** action implements a user story from specification to tested code
via `UzzoCodeAgent` (see `UzzoAgentBuilder.CodeAgentBuilder()`), using the
shared `uzz/skills/code/SKILL.md` skill.

Given a target story and project context (scope, coding standards, development
framework), the agent works the story to completion by following the
implementation workflow defined in the skill (planning, test-driven
implementation, and status/notes updates in the knowledge base).

Stories and their acceptance criteria are the primary input to this action; see
[Story](user-stories) and the [knowledge base](../knowledge-base).
