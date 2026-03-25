package com.aisistent.meta.store;

import org.springframework.stereotype.Component;

/**
 * Simple in-memory token store for testing purposes.
 *
 * Stores a single user access token. In a real application this would be backed
 * by a database or secure session storage, and would support multiple users.
 */
@Component
public class TokenStore {

    private volatile String accessToken;

    /** Save the user access token. */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /** Retrieve the stored access token, or null if none has been stored. */
    public String getAccessToken() {
        return accessToken;
    }

    /** Returns true if an access token is currently stored. */
    public boolean hasToken() {
        return accessToken != null && !accessToken.isBlank();
    }
}
