# Notes — ask/US-000009

- `AskConfirmationToolTest` drives the tool with `execute tool AskConfirmation with arguments: {"question": "..."}` (JSON object arguments need xtest 3.14.1 or later) and reads the tool result from the last request seen by `DummyChatModelListener`.
- It uses the shared `PresetAskTools` (recording implementation with preset answers or a preset failure).
- Tests run and green (confirmed by the developer): the boolean result reaches the model as `true` / `false`.

### Keywords
- notes
- ask-confirmation
- testing
