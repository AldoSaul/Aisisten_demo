package com.leads.integration.dto;

public record IntegrationConnectCallbackResponse(
    boolean success,
    String provider,
    String message,
    String status,
    String authFlowStatus
) {
}

