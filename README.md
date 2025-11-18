# Spring AI Kafka Event Summarizer

A Spring Boot application that uses AI to generate concise summaries of live event messages from Kafka topics. This demo showcases the integration of Spring AI with Anthropic's Claude model and Confluent Kafka through the Model Context Protocol (MCP).

## Overview

This application consumes messages from a Kafka topic containing audience observations at live events, uses AI to generate concise summaries (up to 10 words), and publishes these summaries back to Kafka for display on LED screens.

## Features

- **AI-Powered Summarization**: Uses Anthropic's Claude Sonnet 4.5 model to generate intelligent, concise summaries
- **Kafka Integration**: Connects to Confluent Cloud Kafka via MCP Server for consuming and producing messages
- **Chat Memory**: Maintains conversation context using message window chat memory
- **Custom Logging**: Includes a configurable advisor for logging AI requests and responses
- **Command-Line Application**: Runs as a non-web application with automatic execution on startup

## Prerequisites

- **Java 25** or higher
- **Gradle** (wrapper included)
- **Anthropic API Key**: Required for Claude AI model access
- **Confluent Cloud Access**: 
  - A [Confluent Cloud](https://www.confluent.io/confluent-cloud/) account with an active Kafka cluster
  - Kafka cluster API credentials (configured in the MCP server)
  - Access to the [Confluent MCP Server](https://github.com/confluentinc/mcp-confluent) instance at `https://mcp-confluent.ai-assisted.engineering`

## Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd demo
```

2. Set up environment variables:
```bash
export ANTHROPIC_API_KEY=your_anthropic_api_key_here
```

Alternatively, create a `.env` file in the project root:
```
ANTHROPIC_API_KEY=your_anthropic_api_key_here
```

## Configuration

The application is configured via `src/main/resources/application.yaml`:

### AI Configuration
- **Model**: Claude Sonnet 4.5 (`claude-sonnet-4-5-20250929`)
- **API Key**: Set via `ANTHROPIC_API_KEY` environment variable

### MCP Configuration
- **MCP Server URL**: `https://mcp-confluent.ai-assisted.engineering`
- **Connection Type**: Synchronous with SSE (Server-Sent Events)
- **Request Timeout**: 60 seconds

### Kafka Topics
- **Input Topic**: `user_messages` (audience observations)
- **Output Topic**: `llm_summaries` (AI-generated summaries)

### Logging
- Root level: WARN
- Application level: DEBUG
- Spring AI level: INFO
- MCP level: INFO

## Running the Application

### Using Gradle Wrapper (Recommended)

```bash
./gradlew bootRun
```

### Using Gradle Build

```bash
./gradlew build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

## How It Works

1. **Startup**: The application starts and executes the `CommandLineRunner` bean
2. **Message Consumption**: Consumes messages from the last 5 minutes from the `user_messages` Kafka topic
3. **AI Processing**: Sends messages to Claude AI with instructions to generate a concise summary
4. **Summary Generation**: AI creates a single, family-friendly summary (up to 10 words)
5. **Publishing**: Publishes the summary to the `llm_summaries` topic in JSON format with fields:
   - `summary`: The generated text
   - `timestamp`: When the summary was created
   - `messageCount`: Number of messages processed

## Confluent MCP Server

This application leverages the [Confluent MCP Server](https://github.com/confluentinc/mcp-confluent) to enable AI models to interact with Confluent Cloud Kafka clusters through the Model Context Protocol (MCP).

### What is Confluent MCP Server?

The Confluent MCP Server is an open-source implementation that bridges AI applications with Confluent Cloud, allowing AI models to:
- **Consume messages** from Kafka topics with configurable time windows
- **Produce messages** to Kafka topics
- **List topics** and inspect cluster metadata
- **Query schemas** from Schema Registry

### Key Features

- **Seamless Integration**: Works with any MCP-compatible AI framework (like Spring AI)
- **Secure Access**: Uses Confluent Cloud API keys for authentication
- **Real-time Operations**: Enables AI models to interact with streaming data in real-time
- **Tool-based Interface**: Exposes Kafka operations as callable tools for AI models

### Configuration

The MCP server is configured in `application.yaml` to connect to a hosted instance at `https://mcp-confluent.ai-assisted.engineering`. This server instance is pre-configured with Confluent Cloud credentials and provides access to the Kafka cluster.

For more information, visit the [Confluent MCP Server GitHub repository](https://github.com/confluentinc/mcp-confluent).

## Confluent Cloud

[Confluent Cloud](https://www.confluent.io/confluent-cloud/) is a fully managed, cloud-native Apache Kafka service that provides:

- **Fully Managed Kafka**: No infrastructure management required
- **Global Availability**: Deploy clusters across multiple cloud providers (AWS, Azure, GCP)
- **Enterprise Security**: Built-in encryption, authentication, and authorization
- **Schema Registry**: Centralized schema management for data governance
- **Stream Processing**: Integrated ksqlDB for real-time data processing
- **Monitoring & Alerts**: Comprehensive observability and alerting capabilities

This application connects to Confluent Cloud through the MCP Server, enabling AI-powered operations on production-grade Kafka infrastructure without managing the underlying complexity.

## Technologies Used

- **Spring Boot 3.5.7**: Application framework
- **Spring AI 1.1.0**: AI integration framework
- **Anthropic Claude**: AI model for text generation
- **Model Context Protocol (MCP)**: Protocol for AI-tool integration
- **Confluent Kafka**: Message streaming platform
- **Gradle**: Build automation tool
- **Java 25**: Programming language
- **JUnit 5**: Testing framework

## Project Structure

```
demo/
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java          # Main application class
│   │   │   └── SimpleLoggerAdvisor.java      # Custom logging advisor
│   │   └── resources/
│   │       ├── application.yaml              # Application configuration
│   │       └── logback-spring.xml            # Logging configuration
│   └── test/
│       └── java/com/example/demo/
│           └── DemoApplicationTests.java     # Unit tests
├── build.gradle.kts                          # Gradle build configuration
├── settings.gradle.kts                       # Gradle settings
├── HELP.md                                   # Reference documentation
└── README.md                                 # This file
```

## Development

### Running Tests

```bash
./gradlew test
```

### Building the Project

```bash
./gradlew build
```

### Customizing the Prompt

To modify the AI behavior, edit the prompt in `DemoApplication.java`:
- Change the summary length requirement
- Adjust the tone or style guidelines
- Modify content filtering rules
- Change the time window for message consumption

## Troubleshooting

### API Key Issues
- Ensure `ANTHROPIC_API_KEY` is set correctly
- Verify the API key has sufficient credits and permissions

### Kafka Connection Issues
- Check network connectivity to the MCP server
- Verify the MCP server is accessible at `https://mcp-confluent.ai-assisted.engineering`
- Ensure proper Kafka cluster credentials are configured in the MCP server

### Logging
- Enable DEBUG logging for detailed information: Set `logging.level.com.example.demo: DEBUG`
- Check `SimpleLoggerAdvisor` logs for AI request/response details

## Additional Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/3.5.7/)
- [Spring AI Documentation](https://docs.spring.io/spring-ai/reference/)
- [Model Context Protocol](https://docs.spring.io/spring-ai/reference/api/mcp/mcp-client-boot-starter-docs.html)
- [Anthropic Claude API](https://docs.anthropic.com/)
- [Confluent Cloud](https://www.confluent.io/confluent-cloud/)
- [Confluent MCP Server GitHub](https://github.com/confluentinc/mcp-confluent)
- [Confluent Kafka Documentation](https://docs.confluent.io/)
- [Gradle Documentation](https://docs.gradle.org)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Version

**Version**: 0.0.1-SNAPSHOT
