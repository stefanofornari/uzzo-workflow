# Notes — skill/US-000011

- Skills API: artifact `langchain4j-skills` 1.21.0-beta31 (the beta suffix is how LangChain4j versions its beta modules, 1.21.0 is the matching core release), package `dev.langchain4j.skills`; classes are `@Experimental`.
- The skill is built with `Skill.builder()` from `AgentSystemMessage.loadSkill("uzz/skills/kb/SKILL.md")` instead of `ClassPathSkillLoader`, so that a missing skill fails like the others (`IllegalStateException`) and tests can change where it is resolved from through `AgentSystemMessage.resolve`. The resource path keeps the folder name `kb`; the skill name (frontmatter, catalogue, `activate_skill` argument) is `uzzo-kb`.
- The description in the catalogue is a constant in `KbSkill`, copied from the skill frontmatter; keep it in sync if the skill changes.
- `KbSkill` loads the skill every time the system message or the tools are needed (LangChain4j calls both on each invocation), so nothing is read when the builder is called and a missing skill fails on the agent method call. The system message resolves the agent's own skill first, so the existing missing-skill messages are unchanged.
- Verified in the LangChain4j sources (1.21.0): the system message is prepared before the tool context is created; `activate_skill` takes `skill_name` and returns the skill content as the tool result text; tool providers and `.tools(...)` are combined.
- Not verified: compilation and tests; Maven is not reachable from here. Run `mvn test -pl uzzo-workflow` and set the story to `DONE` (index and story file) if green.
- `PresetAskTools` (test helper) is now public with public fields, because `UzzoKbSkillTest` lives in another package.

### Keywords
- notes
- uzzo-kb
- skills
- tool-mode
