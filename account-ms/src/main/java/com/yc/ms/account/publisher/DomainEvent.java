package com.yc.ms.account.publisher;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DomainEvent extends ApplicationEvent {

    public DomainEvent() {
        super(new HashMap<>());
    }

    public DomainEvent(Map<String, ?> source) {
        super(source);
    }

    private Map<String, ?> data;

}
