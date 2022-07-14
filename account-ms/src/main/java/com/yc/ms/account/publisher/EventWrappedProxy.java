package com.yc.ms.account.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.function.Supplier;

@Component
public class EventWrappedProxy {

    @Autowired
    private EventRepository eventRepository;

    @Transactional
    @SneakyThrows
    public <R, T> T execute(Supplier<T> repositoryFunction) {
        T response = repositoryFunction.get();
        eventRepository.save(
                EventEntity.builder()
                        .data(new ObjectMapper().registerModule(new JavaTimeModule())
                                .writeValueAsString(response)).status(EventStatus.POSTED)
                        .build()
        );
        return response;
    }
}
