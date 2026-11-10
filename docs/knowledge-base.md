---
title: Knowledge Base
layout: default
nav_order: 3
---

# Uzz Knowledge Base

The knowledge base (KB) is a structured Markdown repository that is the single
source of truth for the whole workflow. All agents read from and write to it, so
scoping, story writing, and coding stay consistent.

## Structure

To create one in a project, use the
[NetBeans plugin](https://stefanofornari.github.io/nb-uzzo-workflow/instructions).
To install the bundled Uzzo workflow skills in AI coding tools, see
[Installing skills](installing-skills).

The KB has four top-level directories:

| Directory | Purpose |
|-----------|---------|
| `library/` | General reference: development practices, language guides, frameworks, standard definitions |
| `project/` | Project-specific information: name, version, summary, team, timeline, repos, architecture |
| `skills/` | Uzzo workflow skills (reusable agent instructions) |
| `stories/` | User stories (one per file), organized by domain/feature |

## Rules

- **No root files**: every article lives inside one of the four directories
- **Subdirectories**: group articles into nested folders as appropriate
- **Full relative paths**: reference articles by complete path, e.g.
  `stories/authentication/US101-dark-mode.md`
- **Keywords section**: every article ends with a `### Keywords` section of
  search and categorization tags

## Stories

Each file in `stories/` holds exactly **one** user story, following the
[story template](https://github.com/stefanofornari/uzzo-workflow/blob/main/src/main/uzz/skills/kb/references/user_story.md).
Stories are grouped by domain (e.g. `stories/backend/`, `stories/frontend/`).

- `stories/index.md` is a table of all stories (id and title) with their
  development status: `TODO`, `WIP`, `DONE`
- `scope.md`, under `stories/` or a domain folder, is a descriptive narrative of
  what the project or domain wants to achieve, including constraints and main
  design decisions
