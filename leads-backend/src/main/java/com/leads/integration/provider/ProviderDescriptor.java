package com.leads.integration.provider;

import com.leads.integration.model.ProviderType;

public record ProviderDescriptor(
    ProviderType providerType,
    String key,
    String displayName,
    boolean supportsConnect,
    boolean supportsWebhooks,
    boolean supportsAssetSync
) {
}

