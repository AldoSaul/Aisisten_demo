package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class CustomStubIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.CUSTOM;
    }

    @Override
    protected String displayName() {
        return "Generic Provider (Stub)";
    }
}

