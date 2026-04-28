package com.leads.integration.dto;

import java.time.LocalDateTime;

public record NormalizedWebhookEvent(
    String provider,
    String externalEventId,
    String eventType,
    String tenantLookupKey,
    String payloadJson,
    LocalDateTime receivedAt
) {
}

