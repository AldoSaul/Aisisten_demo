package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class MetaIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.META;
    }

    @Override
    protected String displayName() {
        return "Meta (Phase 1 Stub)";
    }
}

