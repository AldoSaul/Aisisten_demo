package com.leads.integration.provider;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.leads.integration.model.ProviderType;

@Component
public class IntegrationProviderRegistry {

    private final Map<ProviderType, IntegrationProviderHandler> handlersByType;
    private final Map<String, IntegrationProviderHandler> handlersByKey;

    public IntegrationProviderRegistry(Collection<IntegrationProviderHandler> handlers) {
        this.handlersByType = handlers.stream()
            .collect(Collectors.toMap(
                h -> h.descriptor().providerType(),
                Function.identity(),
                (existing, replacement) -> existing
            ));

        this.handlersByKey = handlers.stream()
            .collect(Collectors.toMap(
                h -> h.descriptor().key(),
                Function.identity(),
                (existing, replacement) -> existing
            ));
    }

    public Collection<ProviderDescriptor> listProviders() {
        return handlersByType.values().stream().map(IntegrationProviderHandler::descriptor).toList();
    }

    public IntegrationProviderHandler requireByType(ProviderType providerType) {
        IntegrationProviderHandler handler = handlersByType.get(providerType);
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported provider: " + providerType.name());
        }
        return handler;
    }

    public IntegrationProviderHandler requireByKey(String providerKey) {
        String normalizedKey = providerKey == null ? "" : providerKey.trim().toLowerCase();
        IntegrationProviderHandler handler = handlersByKey.get(normalizedKey);
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported provider: " + providerKey);
        }
        return handler;
    }

    public Optional<IntegrationProviderHandler> findByType(ProviderType providerType) {
        return Optional.ofNullable(handlersByType.get(providerType));
    }
}

