package com.leads.integration.provider;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.leads.integration.model.ProviderType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phase 1 — Task 1.4: Verify that the Meta provider stub correctly handles
 * webhook challenge verification (hub.mode=subscribe, hub.verify_token, hub.challenge).
 *
 * The real signature validation will be implemented in Phase 2.
 * The stub returns true for all signatures — this test validates the contract.
 */
class MetaIntegrationProviderTest {

    private MetaIntegrationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new MetaIntegrationProvider();
    }

    @Test
    void descriptor_returnsMetaProviderType() {
        assertThat(provider.descriptor().providerType()).isEqualTo(ProviderType.META);
    }

    @Test
    void descriptor_keyIsLowercase() {
        assertThat(provider.descriptor().key()).isEqualTo("meta");
    }

    @Test
    void resolveVerificationChallenge_returnsHubChallenge() {
        Map<String, String> params = Map.of(
            "hub.mode", "subscribe",
            "hub.verify_token", "any-token",
            "hub.challenge", "CHALLENGE_12345"
        );
        String result = provider.resolveVerificationChallenge(params);
        assertThat(result).isEqualTo("CHALLENGE_12345");
    }

    @Test
    void resolveVerificationChallenge_returnsMissingWhenChallengeAbsent() {
        Map<String, String> params = Map.of("hub.mode", "subscribe");
        String result = provider.resolveVerificationChallenge(params);
        assertThat(result).isEqualTo("");
    }

    @Test
    void validateWebhookSignature_stubAlwaysReturnsTrue() {
        // Stub implementation returns true — Phase 2 will replace with HMAC-SHA256
        assertThat(provider.validateWebhookSignature("sha256=anything", "{}")).isTrue();
        assertThat(provider.validateWebhookSignature(null, "{}")).isTrue();
    }
}
