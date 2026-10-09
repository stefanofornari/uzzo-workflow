# LangChain4j AI Services — Integration Guide for the Scope Agent

> How to wire LangChain4j `AiServices` to implement the Scope Agent's conversational capabilities within the Viber `Viber`-`Actor` framework.
> Source: https://docs.langchain4j.dev/tutorials/ai-services

---

## Goal

Use LangChain4j's **AI Services** API to provide the Scope Agent with:
- Multi-turn conversational memory (Interrogative Clarification Engine)
- Declarative interface that maps directly to Viber Actor behaviour
- Tool integration for persistence (writing scope documents to disk)

---

## Maven Dependencies

```xml
<!-- Core LangChain4j (AI Services, ChatMemory, Tools) -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>1.0.0-beta3</version>
</dependency>

<!-- LLM provider — replace with the provider used in this project -->
<!-- Example: OpenAI -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

> Check [Maven Central](https://central.sonatype.com/artifact/dev.langchain4j/langchain4j) for the latest release.
> No Spring Boot is used — this is a plain Java / ViberFX / ViberCLI integration.

---

## Recommended Architecture for the Scope Agent

### Step 1 — Define the Agent Interface

```java
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ScopeAgentService {

    @SystemMessage("""
        You are the Scope Agent. Your role is to engage the developer in a structured
        clarifying dialogue to define the scope of a software project.

        Rules:
        - Analyze the user's prompt for gaps, ambiguities, and missing requirements.
        - Ask targeted clarifying questions in clusters of at most 3 at a time.
        - Never assume unknown technical values — always ask.
        - Once all gaps are resolved, partition items into In-Scope and Out-of-Scope.
        - Generate a scope document using the exact template defined in the skill.
        """)
    String chat(@UserMessage String userMessage);
}
```

### Step 2 — Configure Chat Memory

The clarification dialogue is multi-turn, so memory is mandatory:

```java
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(50);
```

`MessageWindowChatMemory` keeps the last N messages (user + AI turns). 50 messages is a safe default for a scoping session.

### Step 3 — Define Tools (Persistence)

Tools let the LLM autonomously call Java methods to persist the scope document:

```java
import dev.langchain4j.agent.tool.Tool;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScopePersistenceTools {

    @Tool("Write the compiled scope document to the given file path. Creates parent directories if needed.")
    public String writeScopeDocument(String filePath, String markdownContent) throws IOException {
        Path path = Path.of(filePath);
        Files.createDirectories(path.getParent());
        Files.writeString(path, markdownContent);
        return "Scope document saved to: " + filePath;
    }

    @Tool("Check whether a scope document file already exists at the given path.")
    public boolean scopeFileExists(String filePath) {
        return Files.exists(Path.of(filePath));
    }
}
```

### Step 4 — Build and Wire the AI Service

```java
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

ChatModel model = OpenAiChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName("gpt-4o-mini")  // or any supported model
    .build();

ScopeAgentService agent = AiServices.builder(ScopeAgentService.class)
    .chatLanguageModel(model)
    .chatMemory(chatMemory)
    .tools(new ScopePersistenceTools())
    .build();
```

### Step 5 — Drive the Dialogue Loop (Viber Actor Integration)

In Viber's Actor model, each user turn is dispatched as a message to the actor. The actor calls the AI Service and returns the response:

```java
// Inside your Viber Actor's message handler:
public String onUserMessage(String userInput) {
    return agent.chat(userInput);
}
```

The `ChatMemory` is maintained across calls on the same `agent` instance — no manual history management needed.

---

## Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| `MessageWindowChatMemory` (not token-window) | Simpler; scoping sessions are short-lived and bounded |
| `@SystemMessage` on the interface | Keeps the agent persona centrally defined; survives refactors |
| `@Tool` for persistence | Lets the LLM decide when the scope is ready to save, without hard-wiring the control flow |
| No Spring Boot | Project runs on plain Java + ViberFX/ViberCLI; Spring DI is out-of-scope |

---

## Conversation Flow Mapping to User Stories

| User Story | LangChain4j component |
|------------|-----------------------|
| [analysis/US-000001](../../stories/analysis/US-000001/US-000001.md) — Accept prompt | `agent.chat(userInput)` first call |
| [analysis/US-000002](../../stories/analysis/US-000002/US-000002.md) — Gap detection | `@SystemMessage` instructs the model |
| [analysis/US-000003](../../stories/analysis/US-000003/US-000003.md) — Constraint identification | `@SystemMessage` rules + model reasoning |
| [clarify/US-000004](../../stories/clarify/US-000004/US-000004.md) — Generate questions | Model generates questions in `chat()` response |
| [clarify/US-000005](../../stories/clarify/US-000005/US-000005.md) — Paced clusters (≤3) | `@SystemMessage` rule: "clusters of at most 3" |
| [clarify/US-000006](../../stories/clarify/US-000006/US-000006.md) — Completeness check | `MessageWindowChatMemory` retains full context |
| [boundary/US-000007](../../stories/boundary/US-000007/US-000007.md) — In-scope partition | Model reasoning within `chat()` |
| [boundary/US-000008](../../stories/boundary/US-000008/US-000008.md) — Scope guarding | `@SystemMessage` rule: "anything not explicit = out-of-scope" |
| [compile/US-000009](../../stories/compile/US-000009/US-000009.md) — Generate document | Model generates Markdown in `chat()` response |
| [compile/US-000010](../../stories/compile/US-000010/US-000010.md) — Populate fields | Model fills template guided by `@SystemMessage` |
| [compile/US-000011](../../stories/compile/US-000011/US-000011.md) — Technical context | No-speculation rule in `@SystemMessage` |
| [persist/US-000012](../../stories/persist/US-000012/US-000012.md) — Write new file | `@Tool writeScopeDocument(...)` |
| [persist/US-000013](../../stories/persist/US-000013/US-000013.md) — Update existing file | `@Tool scopeFileExists(...)` + `writeScopeDocument(...)` |

---

## References

- [LangChain4j Overview](overview.md)
- [AI Services Tutorial](https://docs.langchain4j.dev/tutorials/ai-services)
- [Tools Tutorial](https://docs.langchain4j.dev/tutorials/tools)
- [Chat Memory Tutorial](https://docs.langchain4j.dev/tutorials/chat-memory)
- [Agents and Agentic AI](https://docs.langchain4j.dev/tutorials/agents)

### Keywords
- langchain4j
- ai-services
- chat-memory
- tools
- function-calling
- scope-agent
- viber
- actor
- java
- maven
- conversational-agent
- system-message
