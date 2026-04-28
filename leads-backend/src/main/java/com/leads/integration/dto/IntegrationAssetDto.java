package com.leads.integration.dto;

import java.time.LocalDateTime;

public record IntegrationAssetDto(
    Long id,
    Long connectionId,
    Long accountId,
    String provider,
    String assetType,
    String externalAssetId,
    String displayName,
    boolean active,
    LocalDateTime lastSyncedAt
) {
}

