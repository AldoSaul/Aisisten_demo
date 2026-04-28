package com.leads.integration.model;

public enum ProviderType {
    META,
    WHATSAPP_CLOUD,
    INSTAGRAM,
    FACEBOOK_PAGES,
    TIKTOK,
    CUSTOM;

    public String key() {
        return name().toLowerCase().replace('_', '-');
    }

    public static ProviderType fromKey(String providerKey) {
        if (providerKey == null || providerKey.isBlank()) {
            throw new IllegalArgumentException("Provider key is required");
        }
        String normalized = providerKey.trim().toUpperCase().replace('-', '_');
        return ProviderType.valueOf(normalized);
    }
}

