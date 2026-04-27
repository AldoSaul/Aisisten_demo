package com.leads.integration.provider;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class TikTokIntegrationProvider extends AbstractStubIntegrationProvider {

    @Override
    protected ProviderType providerType() {
        return ProviderType.TIKTOK;
    }

    @Override
    protected String displayName() {
        return "TikTok (Phase 1 Stub)";
    }
}

