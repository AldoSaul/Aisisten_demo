package com.leads.integration.dto;

public record IntegrationConnectionSummaryDto(
    Long id,
    String provider,
    String status,
    String authFlowStatus
) {
}

