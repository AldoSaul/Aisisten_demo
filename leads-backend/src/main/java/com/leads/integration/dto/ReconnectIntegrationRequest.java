package com.leads.integration.dto;

import jakarta.validation.constraints.NotNull;

public record ReconnectIntegrationRequest(
    @NotNull Long tenantId
) {
}

