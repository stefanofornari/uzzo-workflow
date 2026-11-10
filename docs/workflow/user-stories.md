---
title: Story
layout: default
parent: The Workflow
nav_order: 2
---

# Story action

The **story** action generates and refines user stories from a scope via
`UzzoStoryAgent` (see `UzzoAgentBuilder.StoryAgentBuilder()`), using the shared
`uzz/skills/story/SKILL.md` skill.

A user story is a short description of a feature told from the perspective of
the person who wants it. The standard format, popularized by Mike Cohn:

> **As a [role], I want [goal/desire] so that [benefit/value].**

## INVEST

| Principle | Description |
|-----------|-------------|
| **Independent** | Stories can be prioritized flexibly |
| **Negotiable** | Details are settled through conversation, not fixed in a contract |
| **Valuable** | Each story delivers value to the user or customer |
| **Estimable** | The team can estimate the effort |
| **Small** | Small enough to complete in one iteration |
| **Testable** | Clear acceptance criteria exist |

## In Uzzo Workflow

Stories are produced to be INVEST-compliant with clear acceptance criteria and
are the primary input of the **code** action. Each story lives in its own file
in the [knowledge base](../knowledge-base), following the
[story template](https://github.com/stefanofornari/uzzo-workflow/blob/main/src/main/uzz/skills/kb/references/user_story.md).

## References

- [User story (Wikipedia)](https://en.wikipedia.org/wiki/User_story)
- [INVEST criteria (Wikipedia)](https://en.wikipedia.org/wiki/INVEST_(mnemonic))
- Mike Cohn, *User Stories Applied: For Agile Software Development* (2004)
