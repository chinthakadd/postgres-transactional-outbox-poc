package com.yc.eda.event.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Autowired
    private KafkaProperties kafkaProperties;

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name(kafkaProperties.getTemplate().getDefaultTopic()).build();
    }
}
