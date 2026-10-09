# US-000001 Implementation Notes

## Story Assessment
- **Story Size**: Small — foundational component, single responsibility (prompt intake)
- **Domain**: Agent initialization and first-turn messaging
- **Complexity**: Low (basic AI Services interface with validation)
- **Verdict**: ✅ Ready to implement (within 1 day estimate)

## Implementation Strategy

### Target Files
- New: `src/main/java/ste/uzzo/scope/agent/ScopeAgentService.java` — AI Service interface
- New: `src/test/java/ste/uzzo/scope/agent/ScopeAgentServiceTest.java` — TDD tests

### Data / State Changes
- Prompt state: Stored via `ChatMemory` (LangChain4j internal)
- First turn: User sends prompt → Agent acknowledges and stores internally

### Edge Case Handling
- Empty string → reject with validation error
- Whitespace-only string → reject with validation error
- Valid text → acknowledge and store in memory

### Design Notes
- Use LangChain4j `@SystemMessage` for agent persona
- Use `@UserMessage` annotation on `chat()` method (mapped to user input)
- `MessageWindowChatMemory` automatically retains the prompt for subsequent turns
- No explicit storage needed — memory layer handles persistence
- Per coding standard: use `Safe.requireNonBlank()` for input validation before passing to LLM

## Architecture Alignment
- Reference: `uzz/library/langchain4j/ai-services-scope-agent.md` (Step 1-2)
- Maps to: "Accept prompt" → `agent.chat(userInput)` first call
- No Maven changes needed (LangChain4j already in pom.xml)

## Acceptance Criteria Breakdown
1. ✅ SC1: Accept well-formed prompt, acknowledge, store
2. ✅ SC2: Reject empty/whitespace, remain waiting

## Implementation Complete ✅

### Files Created
1. **`src/main/java/ste/uzzo/agent/scope/ScopeAgentService.java`**
   - Interface with default `scope()` method (validation)
   - Internal `_scope_()` method (abstract, for LangChain4j AiServices)
   - `@SystemMessage` decorator with agent persona

2. **`src/main/java/ste/uzzo/util/Safe.java`**
   - `requireNonBlank(str, paramName)` utility
   - `requireNonNull(obj, paramName)` utility

3. **`src/test/java/ste/uzzo/agent/scope/ScopeAgentServiceTest.java`**
   - 5 validation tests, all passing

### Design Pattern Used
- **Validation at Interface**: Public `scope()` method validates input
- **Internal Delegation**: `_scope_()` receives validated input only
- **LangChain4j Integration**: `_scope_()` will be implemented by AiServices builder

### Test Results
```
Tests run: 5, Failures: 0, Errors: 0
BUILD SUCCESS
```

### Acceptance Criteria ✅
1. ✅ SC1: Developer submits well-formed prompt → agent accepts without error
2. ✅ SC2: Developer submits empty/whitespace → agent rejects with error message
3. ✅ Implicit: Agent remains waiting after rejection (tested)
