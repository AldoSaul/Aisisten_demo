package com.leads.integration.dto;

public record IntegrationWebhookAckResponse(
    String status,
    String provider,
    Long webhookEventLogId
) {
}

