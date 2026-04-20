package com.leads.integration.provider;

public record AssetSyncResult(
    int discoveredAccounts,
    int discoveredAssets,
    String message
) {
}

