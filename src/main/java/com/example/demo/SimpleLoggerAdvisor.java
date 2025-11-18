/*
 * Copyright (c) 2025 Viktor Gamov
 *
 * Licensed under the MIT License. See LICENSE file in the project root for details.
 */

package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public record SimpleLoggerAdvisor(boolean logRequestEnabled, boolean logResponseEnabled) implements CallAdvisor {

  private static final Logger logger = LoggerFactory.getLogger(SimpleLoggerAdvisor.class);

  public SimpleLoggerAdvisor() {
    this(true, true);
  }

  @Override
  public ChatClientResponse adviseCall(final ChatClientRequest chatClientRequest, final CallAdvisorChain callAdvisorChain) {
    if (logRequestEnabled) {
      logRequest(chatClientRequest);
    }
    final ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
    if (logResponseEnabled) {
      logResponse(chatClientResponse);
    }
    return chatClientResponse;
  }

  @Override
  public String getName() {
    return "SimpleLoggerAdvisor";
  }

  @Override
  public int getOrder() {
    return 0;
  }

  private void logRequest(ChatClientRequest request) {
    if (!logRequestEnabled || !logger.isDebugEnabled()) {
      return;
    }
    logger.debug("=== LLM Request ===");
    logger.debug("Request: {}", request);
  }

  private void logResponse(ChatClientResponse chatClientResponse) {
    if (!logResponseEnabled || !logger.isDebugEnabled()) {
      return;
    }
    logger.debug("=== LLM Response ===");
    logger.debug("Response: {}", chatClientResponse);
  }
}
