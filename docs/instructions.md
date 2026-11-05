---
title: Instructions
layout: default
nav_order: 4
---

# Instructions: from download to your first knowledge base

This walkthrough takes you from downloading the Uzzo Workflow NetBeans plugin to
a dummy project that has an Uzzo Knowledge Base (KB) in it. It takes about ten
minutes.

1. TOC
{:toc}

## Prerequisites

- Apache NetBeans
- JDK 21 or later
- Internet access, to download the plugin

> Minimum supported NetBeans version and the exact JDK requirement of the plugin
> are still to be filled in.
{: .todo }

## 1. Download the plugin

1. Open the [nb-uzzo-workflow releases page](https://github.com/stefanofornari/nb-uzzo-workflow/releases).
2. Download the `.nbm` file of the latest release, under **Assets**.

> Confirm the release page and the name of the `.nbm` asset.
{: .todo }

> Screenshot: the GitHub releases page with the `.nbm` asset highlighted.
>
> `![Releases page](assets/images/instructions/01-download.png)`
{: .screenshot }

## 2. Install the plugin in NetBeans

1. In NetBeans, open **Tools → Plugins**.
2. Select the **Downloaded** tab and click **Add Plugins...**.
3. Pick the `.nbm` file you downloaded, then click **Install**.
4. Follow the installer: accept the license and, if prompted, trust the
   unsigned plugin.
5. Restart NetBeans when asked.

> Screenshot: the **Downloaded** tab with the plugin added.
>
> `![Plugins dialog](assets/images/instructions/02-plugins-dialog.png)`
{: .screenshot }

> Screenshot: the installer's final page, before the restart.
>
> `![Install complete](assets/images/instructions/03-install-complete.png)`
{: .screenshot }

## 3. Create a dummy project

You only need a project to hold the knowledge base, so any project type works.
A small Maven project is the simplest.

1. Choose **File → New Project...**.
2. Select **Java with Maven → Java Application** and click **Next**.
3. Name it `uzzo-demo`, pick a location, and keep the default Group Id and
   Version. Click **Finish**.
4. The project appears in the **Projects** window.

> Screenshot: the **New Project** wizard with *Java Application* selected.
>
> `![New project wizard](assets/images/instructions/04-new-project.png)`
{: .screenshot }

> Screenshot: the new `uzzo-demo` project in the **Projects** window.
>
> `![Projects window](assets/images/instructions/05-project-created.png)`
{: .screenshot }

## 4. Add the knowledge base

The knowledge base is a top-level `uzz/` folder in your project, created by the
`uzzo-init` skill. Initialization is additive: it never overwrites or deletes
existing files, so it is safe to run on an existing project.

> Describe how to start initialization from the plugin (menu entry, action or
> agent prompt) and which inputs it asks for (project name, version,
> repository).
{: .todo }

> Screenshot: the action that initializes the knowledge base.
>
> `![Initialize the knowledge base](assets/images/instructions/06-init-kb.png)`
{: .screenshot }

When it finishes, the project contains:

```
uzz/
├── library/
├── project/
│   └── overview.md
├── skills/
└── stories/
    ├── index.md
    ├── scope.md
    └── users.md
```

Values the skill can't know, such as the project name or the team, are left as
bracketed placeholders like `[PROJECT NAME]`, and it lists them in its final
report. Fill them in as you go.

## 5. Explore the knowledge base

Back in the **Projects** window, the `uzz` folder shows as the **Uzzo Knowledge
Base** node, with its own icon. Expand it to see `library`, `project`, `skills`
and `stories`.

> Screenshot: the **Uzzo Knowledge Base** node expanded in the **Projects**
> window.
>
> `![Uzzo Knowledge Base node](assets/images/instructions/07-kb-node.png)`
{: .screenshot }

Open `project/overview.md` and replace the placeholders with your project's
details.

> Screenshot: `overview.md` open in the editor.
>
> `![overview.md](assets/images/instructions/08-overview.png)`
{: .screenshot }

## What's next

- Run the [scoping phase](workflow/scoping) on your idea to produce your first
  `scope.md`.
- Read how the [knowledge base](knowledge-base) is organized and the rules every
  article follows.
- Prefer code to a GUI? See [Getting Started](getting-started).
