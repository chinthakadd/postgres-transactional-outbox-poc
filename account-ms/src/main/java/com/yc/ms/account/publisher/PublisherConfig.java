package com.yc.ms.account.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublisherConfig {

    @Bean
    @Qualifier("publisherObjectMapper")
    public ObjectMapper publisherObjectMapper() {
        return new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
