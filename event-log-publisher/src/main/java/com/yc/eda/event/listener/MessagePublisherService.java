package com.yc.eda.event.listener;

import com.yc.eda.event.repository.EventRepository;
import com.yc.eda.event.repository.EventStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class MessagePublisherService {

    private EventRepository eventRepository;
    private KafkaTemplate kafkaTemplate;

    public MessagePublisherService(EventRepository eventRepository, KafkaTemplate kafkaTemplate) {
        this.eventRepository = eventRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishMessage(Map<String, String> eventMessage) {

        // TODO: To avoid contention, we need to implement an optimistic concurrency approach here.
        // first reader to mark the message as READ should proceed to publish the message.
        // other instances should back out.
        String id = eventMessage.get("id");
        String data = eventMessage.get("data");
        log.info("Event Posted [id = {}, data = {}]", id, data);
        eventRepository.findById(Long.parseLong(id)).ifPresent(
                entity -> {
                    log.info("Event Entity [id = {}, data = {}]", id, data);
                    // TODO: any patterns to make this resilient?
                    kafkaTemplate.sendDefault(data);
                    entity.setStatus(EventStatus.PUBLISHED);
                    log.info("Event [id= {}] published", id);
                    eventRepository.save(entity);
                }
        );
    }
}
