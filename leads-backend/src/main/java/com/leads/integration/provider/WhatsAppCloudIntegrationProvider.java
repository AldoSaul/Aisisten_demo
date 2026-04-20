package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class WhatsAppCloudIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.WHATSAPP_CLOUD;
    }

    @Override
    protected String displayName() {
        return "WhatsApp Cloud (Phase 1 Stub)";
    }
}

