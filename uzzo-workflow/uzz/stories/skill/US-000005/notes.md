# Notes — skill/US-000005

- `AgentSystemMessage` is a regular, extensible class instead of a static utility (the coding standard asks to avoid static methods). `resolve(String)` is the single protected extension point: it maps a resource name to a stream and defaults to the classpath. Tests override it to simulate a failing stream; a host application can override it to serve skills from its own class loader.
- The "resource cannot be read" scenario is kept as a real requirement: the classpath is controlled by the host or framework, so such failures are realistic.
- Breaking change: `AgentSystemMessage.loadSkill` is no longer static; callers use `new AgentSystemMessage().loadSkill(...)`. `UzzoAgentBuilder` shares one instance.
- The missing-skill exception surfaces at the agent method call, not at `TokenStream.start()` (verified in skill/US-000006).
- Tests run by the developer: green.

### Keywords
- notes
- error-handling
- testing
- extension-point
