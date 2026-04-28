package com.leads.integration.provider;

import com.fasterxml.jackson.databind.JsonNode;

public record NormalizedProviderWebhookEvent(
    String externalEventId,
    String tenantLookupKey,
    String eventType,
    JsonNode normalizedPayload
) {
}

