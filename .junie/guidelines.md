# Project Guidelines

## Project Overview

This is a trial project for [Koog](https://github.com/JetBrains/koog) - JetBrains' AI agent framework for Kotlin. The project demonstrates the A2A (Agent-to-Agent) protocol implementation, which enables communication between AI agents.

### What is Koog?

Koog is a Kotlin-based AI agent framework that provides:
- AI Agent creation with LLM integration (supports Google Gemini, OpenAI, etc.)
- Tool system for extending agent capabilities
- A2A protocol for agent-to-agent communication

### Project Purpose

This project serves as a demonstration of:
1. Creating AI agents with custom tools
2. Setting up an A2A server that exposes agent skills
3. Building an A2A client that communicates with the server

## Project Structure

```
koog-trial/
├── agent/          # Standalone AI agent module
├── server/         # A2A server module
├── client/         # A2A client module
├── gradle/         # Gradle wrapper and version catalog
└── .junie/         # Junie configuration
```

### Modules

#### `agent`
Contains a standalone AI agent implementation using Google Gemini 2.5 Flash model with a custom Google Search tool. Demonstrates how to:
- Create an AIAgent with an LLM executor
- Define custom tools (GoogleSearchTool)
- Execute agent runs with prompts

#### `server`
Implements an A2A server that:
- Defines an AgentCard with capabilities and skills
- Uses HTTP JSON-RPC transport on `http://localhost:8080/a2a`
- Processes incoming messages and sends task status events
- Supports streaming, push notifications, and state transition history

#### `client`
Implements an A2A client that:
- Connects to the A2A server
- Resolves and caches agent cards
- Sends messages with streaming support
- Processes task status updates from the server

## Technology Stack

- **Language**: Kotlin 2.3.0-Beta2
- **AI Framework**: Koog 0.5.2
- **HTTP Client/Server**: Ktor 3.3.0
- **Serialization**: kotlinx-serialization 1.9.0
- **Testing**: JUnit Jupiter 6.0.1
- **Build Tool**: Gradle with Kotlin DSL

## Environment Variables

The following environment variables are required for the agent module:

| Variable | Description |
|----------|-------------|
| `GEMINI_API_KEY` | Google Gemini API key for LLM access |
| `GOOGLE_CUSTOM_SEARCH_API_KEY` | Google Custom Search API key |
| `CX` | Google Custom Search Engine ID |

## Running the Project

### Running the Server
```bash
./gradlew :server:run
```
The server will start on `http://localhost:8080/a2a`

### Running the Client
```bash
./gradlew :client:run
```
Make sure the server is running before starting the client.

### Running the Agent
```bash
GEMINI_API_KEY=your_key GOOGLE_CUSTOM_SEARCH_API_KEY=your_key CX=your_cx ./gradlew :agent:run
```

## Development Guidelines

### Code Style
- Follow standard Kotlin coding conventions
- Use kotlinx-serialization annotations for data classes that need serialization
- Prefer suspend functions for async operations
- Use Ktor's CIO engine for HTTP operations

### Testing
- Run tests using: `./gradlew test`
- Tests use JUnit Jupiter framework

### Building
- Build the project: `./gradlew build`
- Clean build: `./gradlew clean build`

## A2A Protocol Overview

The A2A (Agent-to-Agent) protocol enables structured communication between AI agents:

1. **AgentCard**: Describes agent capabilities, skills, and endpoints
2. **Skills**: Discrete functionalities that an agent can perform
3. **Messages**: Communication units containing text or other parts
4. **Tasks**: Work units with status tracking (Submitted → Working → Completed)
5. **Transport**: JSON-RPC over HTTP for communication
