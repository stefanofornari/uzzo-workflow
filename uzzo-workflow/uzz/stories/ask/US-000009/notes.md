# Notes — ask/US-000009

- `AskConfirmationToolTest` drives the tool with `execute tool AskConfirmation with arguments: {"question": "..."}` (JSON object arguments need xtest 3.14.1 or later) and reads the tool result from the last request seen by `DummyChatModelListener`.
- It uses the shared `PresetAskTools` (recording implementation with preset answers or a preset failure).
- Not verified: the text LangChain4j gives the model for a `boolean` tool result; the tests expect `true` / `false`.
- Tests not run yet: run `mvn test -pl uzzo-workflow` and set the story to `DONE` in `index.md` (and in the story file) if green. US-000008 is in the same state.

### Keywords
- notes
- ask-confirmation
- testing
