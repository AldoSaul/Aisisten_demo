package com.leads.integration.dto;

import jakarta.validation.constraints.NotNull;

public record IntegrationConnectStartRequest(
    @NotNull Long tenantId
) {
}

