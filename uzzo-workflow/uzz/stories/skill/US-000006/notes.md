# Notes — skill/US-000006

- The "skill missing from the classpath" scenario was removed from this story: `UzzoAgentBuilder` hard-codes the skill paths, so it cannot be tested through the builders without a refactoring that this story does not need. The behaviour belongs to `AgentSystemMessage.loadSkill` and is specified in skill/US-000005.
- `UzzoAgentBuilderTest` was written against the same `DummyStreamingChatModel` / `DummyChatModelListener` pattern used by the agent tests. It was written in a session without Maven access and then run by the developer: green.

### Keywords
- notes
- agent-builder
- testing
