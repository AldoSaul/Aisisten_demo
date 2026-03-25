package com.aisistent.meta;

import com.aisistent.meta.config.MetaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entry point for the Meta Graph API integration test application.
 *
 * Run this class or use: mvn spring-boot:run
 * Then open http://localhost:8080/auth/meta/login in your browser.
 */
@SpringBootApplication
@EnableConfigurationProperties(MetaProperties.class)
public class MetaIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaIntegrationApplication.class, args);
    }
}
