package com.leads.integration.webhook;

import java.util.Map;

public record WebhookRequestContext(
    String provider,
    Map<String, String> queryParams,
    String signature,
    String rawPayload
) {
}

