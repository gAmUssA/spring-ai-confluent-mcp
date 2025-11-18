/*
 * Copyright (c) 2025 Viktor Gamov
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */

package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.List;

import io.modelcontextprotocol.client.McpSyncClient;

@SpringBootApplication
public class DemoApplication {

  static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
  }

  @Bean
  public CommandLineRunner predefinedQuestions(ChatClient.Builder chatClientBuilder,
                                               ToolCallbackProvider tools,
                                               ConfigurableApplicationContext context,
                                               List<McpSyncClient> mcpSyncClients
  ) {

    return args -> {

      String topic = "user_messages";

      StringBuilder prompt = new StringBuilder();

      prompt
          .append("""
                      You are a writer generating concise summaries for a live event LED display.
                      """)
          .append(String.format("""
                                    Consume messages for the last five minutes from Confluent Kafka topic '%s'\
                                    Use the consume-messages MCP tool with the provided connection details. \
                                    Return the messages as JSON.""",
                                topic
          ))
          .append("those are messages from audience observations of a moment that just happened:\n\n")
          .append("Generate a single concise up to 10 word summary that captures the essence of the events ")
          .append("Do not use quotes or punctuation at the end.\n")
          .append("Avoid process, rude humor, and bad words. assume people are in PG audience. \n")
          .append("Publish summary to `llm_summaries` topic in json format including `summary`,`tiemstamp` and `messageCount`  fields.\n ");

      var chatClient = chatClientBuilder
          .defaultSystem("You are assistant that capable to work Confluent Cloud Kafka Clusters via Confluent MCP Server")
          .defaultAdvisors(new SimpleLoggerAdvisor(true, false))
          .defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpSyncClients))
          .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build()).build())
          .build();

      // Start the chat loop
      System.out.println("\nASSISTANT: " + chatClient.prompt(prompt.toString()) // Get the user input
          .call()
          .content());
    };
  }
}
