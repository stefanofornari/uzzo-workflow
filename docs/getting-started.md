---
title: Getting Started
layout: default
nav_order: 4
---

# Getting started

This page is for developers using the Java API. To try Uzzo from the NetBeans
IDE, see the [NetBeans plugin](https://stefanofornari.github.io/nb-uzzo-workflow/).

## Requirements

- Java 21+
- Maven 3.9+
- LangChain4j 1.21.0+

## Build and test

```bash
mvn clean compile
mvn test
```

## Basic setup

```java
// Configure your LLM provider
ChatModel chatModel = ... // e.g., OpenAI, Anthropic, Ollama

// Implement AskTools for your platform (CLI, web, GUI)
AskTools tools = new MyAskToolsImplementation();

// Create the service
ScopeAgentService agent = new ScopeAgentService(chatModel, tools);
```

## Running a scope analysis

```java
String prompt = """
    Build a web application for e-commerce using Node.js
    on AWS cloud platform with JWT authentication and Stripe payments.
    Architecture: microservices with REST API.
    """;

String response = agent.scope(prompt);
// The agent asks clarifying questions if it detects gaps,
// and returns a structured scope document when complete.
```

## Implementing AskTools

Provide a platform-specific implementation of user interaction:

```java
public class CliAskTools implements AskTools {

    @Override
    public String askQuestion(String question) {
        System.out.print(question + " ");
        return new Scanner(System.in).nextLine();
    }

    @Override
    public String askSingleChoice(String question, List<String> options) {
        // Present options, read selection
    }

    @Override
    public List<String> askMultipleChoice(String question, List<String> options) {
        // Present options, read multiple selections
    }

    @Override
    public boolean askConfirmation(String question) {
        // Present yes/no, read confirmation
    }
}
```
