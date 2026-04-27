package com.leads.integration.dto;

import java.time.LocalDateTime;

public record IntegrationConnectionDto(
    Long id,
    String publicId,
    Long tenantId,
    String provider,
    String status,
    String authFlowStatus,
    String externalConnectionId,
    String lastError,
    LocalDateTime connectedAt,
    LocalDateTime disconnectedAt,
    LocalDateTime lastSyncedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

