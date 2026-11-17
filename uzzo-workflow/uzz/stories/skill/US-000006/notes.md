# Notes — skill/US-000006

- The "skill missing from the classpath" scenario was dropped by mistake and put back. It became testable once `AgentSystemMessage` got its `resolve()` extension point (skill/US-000005): the builders now have overloads taking an `AgentSystemMessage`.
- The exception surfaces at the agent method call, not at `TokenStream.start()`: confirmed by the tests.
- `UzzoAgentBuilderTest` was written against the same `DummyStreamingChatModel` / `DummyChatModelListener` pattern used by the agent tests. The three skill-binding tests were run by the developer: green. The three missing-skill tests were run too: green.

### Keywords
- notes
- agent-builder
- testing
