package com.aisistent.meta.service;

import com.aisistent.meta.config.MetaProperties;
import com.aisistent.meta.store.TokenStore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * Centralises every HTTP call to the Meta (Facebook) Graph API.
 *
 * All requests go through {@link RestTemplate} and return the raw JSON
 * as a {@code Map<String, Object>} so the caller can decide how to present it.
 */
@Service
public class MetaApiService {

    private static final Logger log = LoggerFactory.getLogger(MetaApiService.class);

    /** Base URL for the Graph API (v25.0). */
    private static final String GRAPH_BASE = "https://graph.facebook.com/v25.0";

    private final MetaProperties metaProperties;
    private final TokenStore tokenStore;
    private final RestTemplate restTemplate;

    public MetaApiService(MetaProperties metaProperties, TokenStore tokenStore) {
        this.metaProperties = metaProperties;
        this.tokenStore = tokenStore;
        this.restTemplate = new RestTemplate();
    }

    // -------------------------------------------------------------------------
    // OAuth helpers
    // -------------------------------------------------------------------------

    /**
     * Builds the Meta OAuth dialog URL that the user must visit to authorise
     * the application.
     *
     * Current scope: {@code ads_read}.
     * To switch to ads_management, simply change the scope string below.
     *
     * @return fully qualified redirect URL
     */
    public String buildOAuthLoginUrl() {
        // Change this value to "ads_management" when needed
        String scope = "ads_read";

        String url = UriComponentsBuilder
                .fromHttpUrl("https://www.facebook.com/v25.0/dialog/oauth")
                .queryParam("client_id", metaProperties.getAppId())
                .queryParam("redirect_uri", metaProperties.getRedirectUri())
                .queryParam("scope", scope)
                .queryParam("response_type", "code")
                .toUriString();

        log.info("Built Meta OAuth login URL (scope={})", scope);
        return url;
    }

    /**
     * Exchanges an authorisation code for an access token via the Graph API
     * token endpoint, stores the token in memory, and returns the full
     * response body.
     *
     * @param code the authorisation code received in the callback
     * @return parsed JSON response from Meta
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> exchangeCodeForToken(String code) {
        String url = UriComponentsBuilder
                .fromHttpUrl(GRAPH_BASE + "/oauth/access_token")
                .queryParam("client_id", metaProperties.getAppId())
                .queryParam("redirect_uri", metaProperties.getRedirectUri())
                .queryParam("client_secret", metaProperties.getAppSecret())
                .queryParam("code", code)
                .toUriString();

        log.info("Exchanging authorisation code for access token");
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        if (response != null && response.containsKey("access_token")) {
            String token = (String) response.get("access_token");
            tokenStore.setAccessToken(token);
            log.info("Access token stored successfully (preview: {}…)", safePreview(token));
        } else {
            log.warn("Token exchange response did not contain access_token: {}", response);
        }

        return response;
    }

    // -------------------------------------------------------------------------
    // Graph API calls
    // -------------------------------------------------------------------------

    /**
     * Calls GET /me to fetch basic profile info for the authenticated user.
     *
     * @return parsed JSON with fields {@code id} and {@code name}
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchCurrentUser() {
        requireToken();

        String url = UriComponentsBuilder
                .fromHttpUrl(GRAPH_BASE + "/me")
                .queryParam("fields", "id,name")
                .queryParam("access_token", tokenStore.getAccessToken())
                .toUriString();

        log.info("Calling GET /me");
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Calls the debug_token endpoint to inspect the stored user token.
     *
     * The app access token is built as {@code APP_ID|APP_SECRET} per Meta docs.
     *
     * @return parsed JSON with token debug information
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> debugToken() {
        requireToken();

        // App access token format: APP_ID|APP_SECRET
        String appAccessToken = metaProperties.getAppId() + "|" + metaProperties.getAppSecret();

        String url = UriComponentsBuilder
                .fromHttpUrl(GRAPH_BASE + "/debug_token")
                .queryParam("input_token", tokenStore.getAccessToken())
                .queryParam("access_token", appAccessToken)
                .toUriString();

        log.info("Calling GET /debug_token");
        return restTemplate.getForObject(url, Map.class);
    }

    /**
     * Fetches details for a specific Ad Account.
     *
     * @param adAccountId numeric ad account ID (without the "act_" prefix)
     * @return parsed JSON with ad account fields
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchAdAccount(String adAccountId) {
        requireToken();

        String url = UriComponentsBuilder
                .fromHttpUrl(GRAPH_BASE + "/act_" + adAccountId)
                .queryParam("fields", "id,account_id,name,account_status,currency,timezone_name")
                .queryParam("access_token", tokenStore.getAccessToken())
                .toUriString();

        log.info("Calling GET /act_{} for ad account details", adAccountId);
        return restTemplate.getForObject(url, Map.class);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    /**
     * Throws an {@link IllegalStateException} if no access token is available.
     */
    private void requireToken() {
        if (!tokenStore.hasToken()) {
            throw new IllegalStateException(
                    "No access token stored. Complete the OAuth flow first: GET /auth/meta/login");
        }
    }

    /**
     * Returns first 10 characters of a token for safe logging.
     * Never logs the full token or the app secret.
     */
    private String safePreview(String token) {
        if (token == null || token.length() <= 10) {
            return "***";
        }
        return token.substring(0, 10);
    }
}
