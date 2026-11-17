# Notes — ask/US-000008

- `DummyChatModel` always sends tool arguments as `{"prompt": "<text>"}` (or `{}`), see `uzz/library/xtest/HOWTO DummyChatModel.md`. `AskSingleChoice` and `AskMultipleChoice` take `question` and `options`, so through the dummy model they would be called with null arguments and the scenarios "invoked with a question and a list of options" could not be verified. The tests therefore use a scripted `StreamingChatModel` (nested in `AskChoiceToolsTest`) that emits the tool call with real JSON arguments. A cleaner long-term fix would be for xtest's `DummyChatModel` to accept JSON arguments; not done here.
- `ScriptedToolCallModel` and `ChoiceAskTools` are nested in the test class; ask/US-000009 (confirmation) can reuse them, extract them to shared test classes at that point.
- Not verified: how LangChain4j serialises the `List<String>` returned by `askMultipleChoice` as the tool result; the test only checks that the selected options appear in it and the others do not.
- Tests written without Maven access: run `mvn test -pl uzzo-workflow` and set the story to `DONE` in `index.md` if green.

### Keywords
- notes
- ask-single-choice
- ask-multiple-choice
- testing
