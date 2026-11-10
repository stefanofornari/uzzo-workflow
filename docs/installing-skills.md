---
title: Installing Skills
layout: default
nav_order: 5
---

# Installing Uzzo skills in AI coding tools

Uzzo workflow skills are shipped as resources in the `uzzo-workflow-kb` artifact
(a jar file,). Each skill is a directory containing a `SKILL.md` file (and
optional supporting files) under `/uzz/skills/<name>/` inside the jar.

This page shows how to obtain the jar from Maven Central and install the skills
for GitHub Copilot, Claude Code, and OpenCode.

## 1. Download the jar from Maven Central

Download the `uzzo-workflow-kb` jar for the version you want:

- Artifact: `com.github.stefanofornari:uzzo-workflow-kb`
- Browse versions: [Maven Central](https://central.sonatype.com/artifact/com.github.stefanofornari/uzzo-workflow-kb)

Example (replace `VERSION` with a released version, e.g. `0.1.0`):

```bash
VERSION=<version>
curl -fL -o uzzo-workflow-kb.jar \
  "https://repo1.maven.org/maven2/com/github/stefanofornari/uzzo-workflow-kb/${VERSION}/uzzo-workflow-kb-${VERSION}.jar"
```

## 2. Extract the skills from the jar

The skills are packaged under `uzz/skills/` in the jar. Extract them to a
temporary location:

```bash
mkdir -p /tmp/uzzo-skills
unzip uzzo-workflow-kb.jar 'uzz/skills/*' -d .
cp -R uzz/skills /tmp/uzzo-skills/
```

After extraction, `/tmp/uzzo-skills/skills` contains the skill directories (for
example `kb/`, `scope/`, `story/`, `code/`, `uzzo-init/`), each with a
`SKILL.md`.

## 3. Install the skills

Choose a **project** location to share skills with a repository, or a
**personal/global** location to make them available across projects on your
machine. Copy the extracted skill directories into the location your tool reads.

### GitHub Copilot

Copilot discovers project skills from `.github/skills/`, `.claude/skills/`, or
`.agents/skills/` in a repository, and personal skills from `~/.copilot/skills/`
or `~/.agents/skills/`.

Project install:

```bash
mkdir -p .github/skills
cp -R /tmp/uzzo-skills/skills/* .github/skills/
```

Personal install:

```bash
mkdir -p ~/.copilot/skills
cp -R /tmp/uzzo-skills/skills/* ~/.copilot/skills/
```

Each copied folder must contain its `SKILL.md` at
`.github/skills/<skill-name>/SKILL.md` (or the equivalent personal path).

### Claude Code

Claude Code loads project skills from `.claude/skills/` and personal skills from
`~/.claude/skills/`.

Project install:

```bash
mkdir -p .claude/skills
cp -R /tmp/uzzo-skills/skills/* .claude/skills/
```

Personal install:

```bash
mkdir -p ~/.claude/skills
cp -R /tmp/uzzo-skills/skills/* ~/.claude/skills/
```

Start a new Claude Code session (or use `/skills` / `/reload-skills` as needed)
to pick up newly added skills.

### OpenCode

OpenCode discovers project skills from `.opencode/skills/` and global skills
from `~/.config/opencode/skills/` (it also reads `.claude/skills/` and
`.agents/skills/` for compatibility).

Project install:

```bash
mkdir -p .opencode/skills
cp -R /tmp/uzzo-skills/skills/* .opencode/skills/
```

Global install:

```bash
mkdir -p ~/.config/opencode/skills
cp -R /tmp/uzzo-skills/skills/* ~/.config/opencode/skills/
```

OpenCode discovers skills when a session starts; open a new session after
installing.

## 4. Verify the installation

- Confirm each skill directory contains `SKILL.md` (e.g. `ls <skills-dir>/kb`).
- Use the tool's skill listing or invoke a skill by name (for example, ask the
  agent to use the Uzzo `kb`/knowledge-base or `scope` skill) to confirm it is
  discovered.
- To update, download the new jar version and repeat the copy, replacing the
  existing skill directories.

## Notes

- The canonical skill sources in this repository live under `src/main/uzz/skills/`
  and are packaged by the `uzzo-workflow-kb` module. Installing from the jar
  gives the same content as a released artifact.
- If you prefer to install directly from a checkout instead of Maven Central,
  copy the contents of `src/main/uzz/skills/` into the same destination
  directories shown above.
