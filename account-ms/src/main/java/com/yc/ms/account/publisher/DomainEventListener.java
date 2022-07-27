package com.yc.ms.account.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DomainEventListener implements ApplicationListener<DomainEvent> {

    private EventRepository eventRepository;
    private ObjectMapper publisherObjectMapper;

    @Autowired
    public DomainEventListener(EventRepository eventRepository,
                               @Qualifier("publisherObjectMapper")
                                       ObjectMapper publisherObjectMapper) {
        this.eventRepository = eventRepository;
        this.publisherObjectMapper = publisherObjectMapper;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(DomainEvent event) {
        log.info("Received Event: {}", event);
        eventRepository.save(
                EventEntity.builder()
                        .data(publisherObjectMapper.writeValueAsString(event.getData())).status(EventStatus.POSTED)
                        .build()
        );
    }
}
