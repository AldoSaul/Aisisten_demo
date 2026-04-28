package com.leads.integration.provider;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

public interface IntegrationProviderHandler {

    ProviderDescriptor descriptor();

    ConnectStartResult startConnect(Long tenantId);

    ConnectCallbackResult handleCallback(Map<String, String> queryParams);

    boolean validateWebhookSignature(String signatureHeader, String rawPayload);

    NormalizedProviderWebhookEvent normalizeWebhook(JsonNode payload);

    AssetSyncResult syncAssets(Long tenantId);

    default String resolveVerificationChallenge(Map<String, String> queryParams) {
        return queryParams.getOrDefault("hub.challenge", "");
    }
}

