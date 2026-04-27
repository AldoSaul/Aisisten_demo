package com.leads.integration.model;

public enum WebhookProcessingStatus {
    RECEIVED,
    INVALID_SIGNATURE,
    NORMALIZED,
    PUBLISHED,
    FAILED
}

