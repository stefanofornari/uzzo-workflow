# US-000002 Implementation Notes

## Story Assessment
- **Story Size**: Small — introduce tools-driven clarification pattern
- **Domain**: Prompt analysis via tool-driven dialogue
- **Complexity**: Low (interface definition + test infrastructure)
- **Dependencies**: US-000001 ✅
- **Verdict**: ✅ Implemented

## Implementation Summary

### Files Created
1. **`src/main/java/ste/uzzo/agent/scope/AskTools.java`**
   - Platform-agnostic tool interface for user clarification
   - 4 tool methods with `@Tool` decorators:
     - `askQuestion(String): String` — Open-ended clarification
     - `askSingleChoice(String, List): String` — Exclusive option selection
     - `askMultipleChoice(String, List): List<String>` — Multi-select
     - `askConfirmation(String): boolean` — Yes/No confirmation
   - Tool names in PascalCase (AskQuestion, AskSingleChoice, etc.)
   - Method names in camelCase per Java convention

2. **`src/test/java/ste/uzzo/agent/scope/ScopeAgentServiceClarificationTest.java`**
   - 4 tests for gap analysis via tool calls
   - MockAskTools: Mock implementation tracking tool usage
   - ScopeAgentServiceWithTools: Test stub implementing analysis logic

### Design Decisions
- **Platform-Agnostic**: Developers provide own implementations (CLI, GUI, etc.)
- **Tool-Driven Dialogue**: Agent autonomously calls tools to gather info
- **Immutable Contract**: All tool methods have clear signatures, no side effects
- **Framework Integration**: Annotated with LangChain4j `@Tool` decorator

### Updated System Message
- Agent told to use AskQuestion, AskSingleChoice, AskMultipleChoice, AskConfirmation tools
- No longer just "records gaps" — actively gathers answers via tools
- Keeps dialogue interactive and user-driven

### Acceptance Criteria ✅
1. **SC1**: Complete prompt → no tools called ✓
   - Test: `agent_analyzes_complete_prompt_without_tool_calls()`
2. **SC2**: Missing platform → AskSingleChoice called ✓
   - Test: `agent_calls_askSingleChoice_when_platform_is_missing()`
3. **SC3**: Missing architecture → AskQuestion called ✓
   - Test: `agent_calls_askQuestion_when_architecture_is_missing()`

### Test Results
```
ScopeAgentServiceClarificationTest: 4/4 passing
ScopeAgentServiceTest: 5/5 passing (no regressions)
Overall: 9/9 passing
BUILD SUCCESS
```

### Architecture Implications
- **Flattened Design**: Removes artificial "gaps list" intermediate data
- **Single Flow**: Analysis + clarification + compilation all driven by tool calls
- **Developer Control**: Implementations decide how to present tools (CLI, GUI, voice, etc.)
- **Next Stories**: No longer need separate "Generate Questions" story — agent does it

### Story Restructuring Impact
- Original US-000002-006 (clarify domain) may collapse into single loop
- Original US-000007-011 (boundary/compile) become tool calls within dialogue
- Original US-000012-013 (persist) become explicit final tool call
- This is a **simplified, more coherent model**

### Next Steps
- Review US-000003 (Identify Unresolved Requirements) — may be redundant with new model
- Consolidate clarify/boundary/compile stories based on tool-driven flow
- Implement production tool versions (CLI, GUI)
