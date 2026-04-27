package com.leads.integration.dto;

import jakarta.validation.constraints.NotNull;

public record AssetSyncRequest(
    @NotNull Long tenantId
) {
}

