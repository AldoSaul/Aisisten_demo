package com.leads.integration.client;

/**
 * Thrown when the Meta Graph API returns a 4xx or 5xx error response.
 */
public class MetaApiException extends RuntimeException {

    private final int graphApiCode;
    private final int httpStatus;

    public MetaApiException(String message, int graphApiCode, int httpStatus) {
        super(message);
        this.graphApiCode = graphApiCode;
        this.httpStatus = httpStatus;
    }

    public int getGraphApiCode() {
        return graphApiCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
