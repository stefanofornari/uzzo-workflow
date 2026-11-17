# Notes — skill/US-000006

- The "skill missing from the classpath" scenario was dropped by mistake and put back. It became testable once `AgentSystemMessage` got its `resolve()` extension point (skill/US-000005): the builders now have overloads taking an `AgentSystemMessage`.
- Where the exception surfaces (agent method call vs `TokenStream.start()`) is assumed to be the call; the new tests will confirm or refute it.
- `UzzoAgentBuilderTest` was written against the same `DummyStreamingChatModel` / `DummyChatModelListener` pattern used by the agent tests. The three skill-binding tests were run by the developer: green. The three missing-skill tests are new and have not been run.

### Keywords
- notes
- agent-builder
- testing
