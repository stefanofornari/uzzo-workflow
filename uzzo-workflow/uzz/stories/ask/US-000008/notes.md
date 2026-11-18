# Notes — ask/US-000008

- `AskSingleChoice` and `AskMultipleChoice` take `question` and `options`, so the tests need `DummyChatModel` to pass a JSON object as tool arguments (`execute tool AskSingleChoice with arguments: {"question": ..., "options": [...]}`). Before xtest 3.14.1 it could only send `{"prompt": "<text>"}`; a first version of the tests used a scripted `StreamingChatModel` for that and was replaced once xtest was fixed.
- The recording tools implementation is the shared test class `PresetAskTools` (extracted from the former nested `ChoiceAskTools` for ask/US-000009).
- Tests run and green (confirmed by the developer): the `List<String>` result is serialised in a way that contains the selected options.

### Keywords
- notes
- ask-single-choice
- ask-multiple-choice
- testing
