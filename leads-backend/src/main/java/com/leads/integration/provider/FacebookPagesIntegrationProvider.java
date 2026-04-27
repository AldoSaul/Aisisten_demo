package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class FacebookPagesIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.FACEBOOK_PAGES;
    }

    @Override
    protected String displayName() {
        return "Facebook Pages (Phase 1 Stub)";
    }
}

