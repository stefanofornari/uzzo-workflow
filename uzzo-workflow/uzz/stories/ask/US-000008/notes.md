# Notes — ask/US-000008

- `AskSingleChoice` and `AskMultipleChoice` take `question` and `options`, so the tests need `DummyChatModel` to pass a JSON object as tool arguments (`execute tool AskSingleChoice with arguments: {"question": ..., "options": [...]}`). Before xtest 3.14.1 it could only send `{"prompt": "<text>"}`; a first version of the tests used a scripted `StreamingChatModel` for that and was replaced once xtest was fixed.
- `ChoiceAskTools` is nested in the test class; ask/US-000009 (confirmation) can reuse it, extract it to a shared test class at that point.
- Not verified: how LangChain4j serialises the `List<String>` returned by `askMultipleChoice` as the tool result; the test only checks that the selected options appear in it and the others do not.
- Tests not run yet in this form: run `mvn test -pl uzzo-workflow` and set the story to `DONE` in `index.md` if green.

### Keywords
- notes
- ask-single-choice
- ask-multiple-choice
- testing
