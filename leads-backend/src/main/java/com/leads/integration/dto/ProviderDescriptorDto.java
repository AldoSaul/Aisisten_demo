package com.leads.integration.dto;

public record ProviderDescriptorDto(
    String provider,
    String displayName,
    boolean supportsConnect,
    boolean supportsWebhooks,
    boolean supportsAssetSync
) {
}

