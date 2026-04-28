package com.leads.integration.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Phase 1 — Task 1.2: Verify that MetaGraphClient builds versioned Graph API URLs
 * using the configured version and never hardcodes a version string.
 */
class MetaGraphClientTest {

    private MetaGraphClient client;

    @BeforeEach
    void setUp() {
        client = buildClientWithVersion("v25.0");
    }

    @Test
    void buildUrl_withLeadingSlash_returnsVersionedUrl() {
        MetaGraphClient c = buildClientWithVersion("v25.0");
        String url = c.buildUrl("/me/accounts");
        assertThat(url).isEqualTo("https://graph.facebook.com/v25.0/me/accounts");
    }

    @Test
    void buildUrl_withoutLeadingSlash_returnsVersionedUrl() {
        MetaGraphClient c = buildClientWithVersion("v25.0");
        String url = c.buildUrl("me/accounts");
        assertThat(url).isEqualTo("https://graph.facebook.com/v25.0/me/accounts");
    }

    @Test
    void buildUrl_respectsConfiguredVersion() {
        MetaGraphClient c = buildClientWithVersion("v20.0");
        String url = c.buildUrl("/me/accounts");
        assertThat(url).startsWith("https://graph.facebook.com/v20.0/");
        assertThat(url).doesNotContain("v25.0");
        assertThat(url).doesNotContain("v19.0");
    }

    @Test
    void buildUrl_doesNotHardcodeOldVersion() {
        MetaGraphClient c = buildClientWithVersion("v25.0");
        String url = c.buildUrl("/oauth/access_token");
        assertThat(url).doesNotContain("v19.0");
        assertThat(url).contains("v25.0");
    }

    @Test
    void getGraphApiVersion_returnsConfiguredVersion() {
        MetaGraphClient c = buildClientWithVersion("v25.0");
        assertThat(c.getGraphApiVersion()).isEqualTo("v25.0");
    }

    /**
     * Creates a MetaGraphClient with the given version injected via field access.
     * This simulates Spring @Value injection for unit tests without a Spring context.
     */
    private MetaGraphClient buildClientWithVersion(String version) {
        MetaGraphClient c = new MetaGraphClient(null);
        try {
            var field = MetaGraphClient.class.getDeclaredField("graphApiVersion");
            field.setAccessible(true);
            field.set(c, version);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set graphApiVersion for test", e);
        }
        return c;
    }
}
