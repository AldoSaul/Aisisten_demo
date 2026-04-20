package com.leads.integration.service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leads.integration.model.IntegrationProvider;
import com.leads.integration.provider.IntegrationProviderRegistry;
import com.leads.integration.provider.ProviderDescriptor;
import com.leads.integration.repository.IntegrationProviderRepository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IntegrationProviderCatalogService {

    private final IntegrationProviderRegistry providerRegistry;
    private final IntegrationProviderRepository providerRepository;

    @PostConstruct
    @Transactional
    public void syncCatalog() {
        Collection<ProviderDescriptor> descriptors = providerRegistry.listProviders();
        descriptors.forEach(descriptor -> {
            IntegrationProvider provider = providerRepository.findByProviderType(descriptor.providerType())
                .orElseGet(() -> IntegrationProvider.builder()
                    .providerType(descriptor.providerType())
                    .createdAt(LocalDateTime.now())
                    .build());

            provider.setDisplayName(descriptor.displayName());
            provider.setEnabled(true);
            provider.setSupportsWebhooks(descriptor.supportsWebhooks());
            provider.setSupportsAssetSync(descriptor.supportsAssetSync());
            providerRepository.save(provider);
        });
    }
}

