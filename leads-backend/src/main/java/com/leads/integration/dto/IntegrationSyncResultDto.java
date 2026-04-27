package com.leads.integration.dto;

public record IntegrationSyncResultDto(
    String provider,
    int discoveredAccounts,
    int discoveredAssets,
    String message
) {
}

