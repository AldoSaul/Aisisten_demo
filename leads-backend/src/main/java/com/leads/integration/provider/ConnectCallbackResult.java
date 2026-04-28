package com.leads.integration.provider;

import com.leads.integration.model.AuthFlowStatus;
import com.leads.integration.model.ConnectionStatus;

public record ConnectCallbackResult(
    boolean success,
    String message,
    AuthFlowStatus authFlowStatus,
    ConnectionStatus connectionStatus
) {
}

