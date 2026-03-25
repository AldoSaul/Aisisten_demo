package com.aisistent.meta.controller;

import com.aisistent.meta.dto.CallbackResponse;
import com.aisistent.meta.dto.ErrorResponse;
import com.aisistent.meta.service.MetaApiService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * REST controller that exposes the Meta OAuth flow and Graph API test endpoints.
 *
 * Endpoints:
 *   GET /auth/meta/login       – redirects to Meta OAuth dialog
 *   GET /auth/meta/callback    – handles the OAuth callback, stores token
 *   GET /auth/meta/test        – calls GET /me with the stored token
 *   GET /auth/meta/debug       – calls debug_token for the stored token
 *   GET /meta/ad-account       – fetches ad account details by id
 */
@RestController
public class MetaController {

    private static final Logger log = LoggerFactory.getLogger(MetaController.class);

    private final MetaApiService metaApiService;

    public MetaController(MetaApiService metaApiService) {
        this.metaApiService = metaApiService;
    }

    // -------------------------------------------------------------------------
    // 1. Login – redirect to Meta OAuth dialog
    // -------------------------------------------------------------------------

    /**
     * GET /auth/meta/login
     *
     * Redirects the browser to Facebook's OAuth consent screen.
     * After the user approves, Meta will redirect back to /auth/meta/callback.
     */
    @GetMapping("/auth/meta/login")
    public void login(HttpServletResponse response) throws IOException {
        String loginUrl = metaApiService.buildOAuthLoginUrl();
        log.info("Redirecting user to Meta OAuth dialog");
        response.sendRedirect(loginUrl);
    }

    // -------------------------------------------------------------------------
    // 2. Callback – exchange code for token
    // -------------------------------------------------------------------------

    /**
     * GET /auth/meta/callback?code=...
     *
     * Called by Meta after a successful user authorisation. Exchanges the
     * authorisation code for an access token and returns a confirmation JSON.
     */
    @GetMapping("/auth/meta/callback")
    public ResponseEntity<?> callback(@RequestParam(required = false) String code) {
        // Validate that the code parameter was provided
        if (code == null || code.isBlank()) {
            log.warn("Callback invoked without authorisation code");
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("missing_code",
                            "The 'code' query parameter is required. " +
                            "Start the flow at /auth/meta/login."));
        }

        try {
            Map<String, Object> tokenResponse = metaApiService.exchangeCodeForToken(code);

            // Extract the token for a safe preview in the response
            String token = (String) tokenResponse.get("access_token");
            String preview = (token != null && token.length() > 10)
                    ? token.substring(0, 10) + "..."
                    : "***";

            CallbackResponse body = new CallbackResponse("Meta account connected", preview);
            return ResponseEntity.ok(body);

        } catch (RestClientResponseException ex) {
            log.error("Failed to exchange code for token: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ErrorResponse("token_exchange_failed", ex.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // 3. Test – call GET /me
    // -------------------------------------------------------------------------

    /**
     * GET /auth/meta/test
     *
     * Calls the Meta Graph API /me endpoint and returns the user's id and name.
     * Requires a valid stored access token (complete the login flow first).
     */
    @GetMapping("/auth/meta/test")
    public ResponseEntity<?> testMe() {
        try {
            Map<String, Object> result = metaApiService.fetchCurrentUser();
            return ResponseEntity.ok(result);

        } catch (IllegalStateException ex) {
            // No token stored yet
            log.warn("Test /me called without stored token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("no_token", ex.getMessage()));

        } catch (RestClientResponseException ex) {
            log.error("Graph API /me call failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ErrorResponse("graph_api_error", ex.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // 4. Debug – inspect the stored token
    // -------------------------------------------------------------------------

    /**
     * GET /auth/meta/debug
     *
     * Calls the debug_token endpoint to inspect the stored user access token.
     * Useful for checking scopes, expiration, and validity.
     */
    @GetMapping("/auth/meta/debug")
    public ResponseEntity<?> debugToken() {
        try {
            Map<String, Object> result = metaApiService.debugToken();
            return ResponseEntity.ok(result);

        } catch (IllegalStateException ex) {
            log.warn("Debug called without stored token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("no_token", ex.getMessage()));

        } catch (RestClientResponseException ex) {
            log.error("Graph API debug_token call failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ErrorResponse("graph_api_error", ex.getMessage()));
        }
    }

    // -------------------------------------------------------------------------
    // 5. Ad Account – fetch ad account details
    // -------------------------------------------------------------------------

    /**
     * GET /meta/ad-account?adAccountId=123456789012345
     *
     * Fetches details for the specified ad account from the Meta Graph API.
     */
    @GetMapping("/meta/ad-account")
    public ResponseEntity<?> adAccount(
            @RequestParam(required = false) String adAccountId) {

        // Validate the adAccountId parameter
        if (adAccountId == null || adAccountId.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("missing_ad_account_id",
                            "Query parameter 'adAccountId' is required. " +
                            "Example: /meta/ad-account?adAccountId=123456789012345"));
        }

        try {
            Map<String, Object> result = metaApiService.fetchAdAccount(adAccountId);
            return ResponseEntity.ok(result);

        } catch (IllegalStateException ex) {
            log.warn("Ad account request without stored token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("no_token", ex.getMessage()));

        } catch (RestClientResponseException ex) {
            log.error("Graph API ad account call failed: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body(new ErrorResponse("graph_api_error", ex.getMessage()));
        }
    }
}
