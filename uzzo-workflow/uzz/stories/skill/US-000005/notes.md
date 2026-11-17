# Notes — skill/US-000005

- The "resource cannot be read" scenario was left out of the acceptance criteria: reading goes through the class loader inside `loadSkill`, so a failing stream cannot be injected without a refactoring this story does not need. The `UncheckedIOException` path stays implemented but untested.
- `AgentSystemMessageTest` was written in a session without Maven access. Status is `DONE` in `index.md` on the assumption that it passes; run `mvn test -pl uzzo-workflow` to confirm and revert to `WIP` if not.

### Keywords
- notes
- error-handling
- testing
