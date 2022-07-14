package com.yc.eda.event.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "eventpub")
@Data
public class EventPubProperties {

    private String replicationSlot;
    private String publication;
    private String eventTable;

}
