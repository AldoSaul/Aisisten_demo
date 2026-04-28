package com.leads.integration.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Central client for Meta Graph API calls.
 *
 * Responsibilities:
 * - Build versioned Graph API URLs using the single configured version.
 * - Execute GET and POST requests with bearer token injection.
 * - Log request paths without leaking tokens, app secrets, or authorization headers.
 * - Map Graph API error responses into MetaApiException.
 *
 * The configured version comes from {@code meta.graph-api-version} (default v25.0).
 * No other class may hardcode a /vXX.X/ path segment.
 */
@Slf4j
@Component
public class MetaGraphClient {

    private static final String GRAPH_API_BASE = "https://graph.facebook.com";

    @Value("${meta.graph-api-version:v25.0}")
    private String graphApiVersion;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MetaGraphClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Builds a versioned Graph API URL for the given path.
     * Example: buildUrl("/me/accounts") -> "https://graph.facebook.com/v25.0/me/accounts"
     */
    public String buildUrl(String path) {
        String cleanPath = path.startsWith("/") ? path : "/" + path;
        return GRAPH_API_BASE + "/" + graphApiVersion + cleanPath;
    }

    /**
     * Executes a GET request against the Graph API.
     * The access token is appended as a query parameter (Meta convention).
     * IMPORTANT: The token is never written to logs.
     *
     * @param path        Graph API path, e.g. "/me/accounts"
     * @param accessToken Bearer access token
     * @return parsed JSON response
     * @throws MetaApiException on 4xx/5xx responses
     */
    public JsonNode get(String path, String accessToken) {
        String url = buildUrl(path) + "?access_token=REDACTED";
        log.debug("Meta Graph API GET {}/{}{}", GRAPH_API_BASE, graphApiVersion, path);
        String actualUrl = buildUrl(path) + "?access_token=" + accessToken;
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                actualUrl, HttpMethod.GET, HttpEntity.EMPTY, JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw mapGraphApiError(path, ex);
        }
    }

    /**
     * Executes a POST request against the Graph API with a JSON body.
     * The Authorization header is injected; it is never written to logs.
     *
     * @param path        Graph API path, e.g. "/{phone-number-id}/messages"
     * @param accessToken Bearer access token
     * @param body        Request body (will be serialized to JSON)
     * @return parsed JSON response
     * @throws MetaApiException on 4xx/5xx responses
     */
    public JsonNode post(String path, String accessToken, Object body) {
        log.debug("Meta Graph API POST {}/{}{}", GRAPH_API_BASE, graphApiVersion, path);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Content-Type", "application/json");
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                buildUrl(path), HttpMethod.POST, entity, JsonNode.class);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            throw mapGraphApiError(path, ex);
        }
    }

    /**
     * Returns the currently configured Graph API version string (e.g. "v25.0").
     */
    public String getGraphApiVersion() {
        return graphApiVersion;
    }

    private MetaApiException mapGraphApiError(String path, HttpClientErrorException ex) {
        String errorMessage = ex.getMessage();
        try {
            JsonNode errorBody = objectMapper.readTree(ex.getResponseBodyAsString());
            JsonNode error = errorBody.path("error");
            if (!error.isMissingNode()) {
                errorMessage = error.path("message").asText(ex.getMessage());
                int code = error.path("code").asInt(0);
                log.warn("Meta Graph API error on path={} code={} message={}",
                    path, code, errorMessage);
                return new MetaApiException(errorMessage, code, ex.getStatusCode().value());
            }
        } catch (Exception parseEx) {
            log.warn("Could not parse Meta Graph API error body for path={}", path);
        }
        return new MetaApiException(errorMessage, 0, ex.getStatusCode().value());
    }
}
