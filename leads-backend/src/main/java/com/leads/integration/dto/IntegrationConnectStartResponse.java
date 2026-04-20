package com.leads.integration.dto;

public record IntegrationConnectStartResponse(
    Long connectionId,
    String provider,
    String state,
    String redirectUrl,
    String status,
    String authFlowStatus
) {
}

