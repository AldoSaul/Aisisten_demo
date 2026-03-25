package com.aisistent.meta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration for RestClient bean.
 */
@Configuration
public class RestClientConfig {

    /**
     * Provides a RestClient.Builder bean for HTTP client operations.
     */
    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
