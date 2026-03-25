package com.aisistent.meta.dto;

/**
 * Response returned after a successful OAuth callback.
 */
public class CallbackResponse {

    private String message;
    private String accessTokenPreview;

    public CallbackResponse() {
    }

    public CallbackResponse(String message, String accessTokenPreview) {
        this.message = message;
        this.accessTokenPreview = accessTokenPreview;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessTokenPreview() {
        return accessTokenPreview;
    }

    public void setAccessTokenPreview(String accessTokenPreview) {
        this.accessTokenPreview = accessTokenPreview;
    }
}
