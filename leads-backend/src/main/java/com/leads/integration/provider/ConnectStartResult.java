package com.leads.integration.provider;

import com.leads.integration.model.AuthFlowStatus;
import com.leads.integration.model.ConnectionStatus;

public record ConnectStartResult(
    String state,
    String redirectUrl,
    AuthFlowStatus authFlowStatus,
    ConnectionStatus connectionStatus
) {
}

