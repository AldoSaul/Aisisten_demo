package com.leads.integration.dto;

import jakarta.validation.constraints.NotNull;

public record DisconnectIntegrationRequest(
    @NotNull Long tenantId
) {
}

