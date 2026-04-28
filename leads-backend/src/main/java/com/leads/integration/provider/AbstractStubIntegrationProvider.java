package com.leads.integration.provider;

import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.leads.integration.model.AuthFlowStatus;
import com.leads.integration.model.ConnectionStatus;
import com.leads.integration.model.ProviderType;

public abstract class AbstractStubIntegrationProvider implements IntegrationProviderHandler {

    protected abstract ProviderType providerType();

    protected abstract String displayName();

    @Override
    public ProviderDescriptor descriptor() {
        ProviderType providerType = providerType();
        return new ProviderDescriptor(
            providerType,
            providerType.key(),
            displayName(),
            true,
            true,
            true
        );
    }

    @Override
    public ConnectStartResult startConnect(Long tenantId) {
        String state = providerType().key() + "-" + tenantId + "-" + UUID.randomUUID();
        return new ConnectStartResult(
            state,
            null,
            AuthFlowStatus.STARTED,
            ConnectionStatus.PENDING
        );
    }

    @Override
    public ConnectCallbackResult handleCallback(Map<String, String> queryParams) {
        return new ConnectCallbackResult(
            true,
            "Stub callback accepted",
            AuthFlowStatus.COMPLETED,
            ConnectionStatus.CONNECTED
        );
    }

    @Override
    public boolean validateWebhookSignature(String signatureHeader, String rawPayload) {
        return true;
    }

    @Override
    public NormalizedProviderWebhookEvent normalizeWebhook(JsonNode payload) {
        String eventId = payload.path("eventId").asText(null);
        String tenantLookupKey = payload.path("tenantId").asText(null);
        String eventType = payload.path("eventType").asText("unknown");
        JsonNode normalized = payload.has("normalized") ? payload.path("normalized") : payload;
        return new NormalizedProviderWebhookEvent(eventId, tenantLookupKey, eventType, normalized);
    }

    @Override
    public AssetSyncResult syncAssets(Long tenantId) {
        return new AssetSyncResult(0, 0, "Stub provider does not fetch remote assets yet");
    }
}

