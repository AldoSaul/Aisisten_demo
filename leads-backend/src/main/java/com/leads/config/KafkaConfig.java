package com.leads.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topics.messages-incoming}")
    private String messagesIncoming;

    @Value("${kafka.topics.messages-processed}")
    private String messagesProcessed;

    @Value("${kafka.topics.leads-new}")
    private String leadsNew;

    @Value("${kafka.topics.integrations-webhook-normalized}")
    private String integrationsWebhookNormalized;

    @Bean
    public NewTopic topicMessagesIncoming() {
        return TopicBuilder.name(messagesIncoming)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic topicMessagesProcessed() {
        return TopicBuilder.name(messagesProcessed)
            .partitions(3)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic topicLeadsNew() {
        return TopicBuilder.name(leadsNew)
            .partitions(1)
            .replicas(1)
            .build();
    }

    @Bean
    public NewTopic topicIntegrationsWebhookNormalized() {
        return TopicBuilder.name(integrationsWebhookNormalized)
            .partitions(3)
            .replicas(1)
            .build();
    }
}
