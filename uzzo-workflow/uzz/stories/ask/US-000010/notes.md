# Notes — ask/US-000010

- Scenarios 1-3 (exception messages) were already covered by `NoAnswerExceptionTest`; scenario 4 only had an `instanceof` check, and scenario 5 had no test. No production code needed to change: the exception and the four tool descriptions already behave as specified.
- Scenario 4 is tested end to end with `DummyStreamingChatModel`: the prompt `execute tool AskQuestion` makes the dummy model call the tool, the tool throws, and the test reads the tool result in the last request received by the model. This relies on the dummy model calling the tool and on LangChain4j passing a `ToolErrorVisibleToLlm` message back as the result; if the test fails, check those two assumptions first.
- Scenario 5 reads the `@Tool` annotations of `AskTools` by reflection; the same test also pins the four tool names (used by ask/US-000007, 000008 and 000009).
- Tests run by the developer: green, so the two assumptions above held.

### Keywords
- notes
- no-answer
- testing
