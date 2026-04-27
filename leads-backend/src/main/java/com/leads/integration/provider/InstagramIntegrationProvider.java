package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class InstagramIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.INSTAGRAM;
    }

    @Override
    protected String displayName() {
        return "Instagram (Phase 1 Stub)";
    }
}

