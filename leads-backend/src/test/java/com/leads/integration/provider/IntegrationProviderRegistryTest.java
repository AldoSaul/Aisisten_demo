package com.leads.integration.provider;

import org.junit.jupiter.api.Test;

import com.leads.integration.model.ProviderType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Phase 1 — Task 1.1: Verify the provider registry correctly resolves all
 * expected providers and rejects unknown keys.
 */
class IntegrationProviderRegistryTest {

    private IntegrationProviderRegistry buildRegistry() {
        return new IntegrationProviderRegistry(java.util.List.of(
            new MetaIntegrationProvider(),
            new InstagramIntegrationProvider(),
            new FacebookPagesIntegrationProvider(),
            new WhatsAppCloudIntegrationProvider(),
            new TikTokIntegrationProvider(),
            new CustomStubIntegrationProvider()
        ));
    }

    @Test
    void listProviders_containsAllExpectedProviders() {
        IntegrationProviderRegistry registry = buildRegistry();
        var keys = registry.listProviders().stream()
            .map(ProviderDescriptor::key)
            .toList();
        assertThat(keys).contains("meta", "instagram", "facebook-pages", "whatsapp-cloud", "tiktok", "custom");
    }

    @Test
    void requireByType_resolvesMetaProvider() {
        IntegrationProviderRegistry registry = buildRegistry();
        IntegrationProviderHandler handler = registry.requireByType(ProviderType.META);
        assertThat(handler).isInstanceOf(MetaIntegrationProvider.class);
    }

    @Test
    void requireByKey_resolvesProviderByString() {
        IntegrationProviderRegistry registry = buildRegistry();
        IntegrationProviderHandler handler = registry.requireByKey("instagram");
        assertThat(handler).isInstanceOf(InstagramIntegrationProvider.class);
    }

    @Test
    void requireByType_throwsForUnregisteredProvider() {
        // Build a registry with only Meta
        IntegrationProviderRegistry registry = new IntegrationProviderRegistry(
            java.util.List.of(new MetaIntegrationProvider())
        );
        assertThatThrownBy(() -> registry.requireByType(ProviderType.TIKTOK))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("TIKTOK");
    }

    @Test
    void requireByKey_throwsForUnknownKey() {
        IntegrationProviderRegistry registry = buildRegistry();
        assertThatThrownBy(() -> registry.requireByKey("unknown-provider"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
