# Notes — skill/US-000005

- Open question, to decide with the developer: the "resource cannot be read" scenario has no test, because `loadSkill` reads through the class loader and offers no way to inject a failing stream. Options to discuss: add a seam (e.g. a `loadSkill` variant taking the stream or the class loader), or reconsider the scenario. It stays in the story until decided.
- `AgentSystemMessageTest` (missing-resource scenario) was written in a session without Maven access and has not been run yet.

### Keywords
- notes
- error-handling
- testing
