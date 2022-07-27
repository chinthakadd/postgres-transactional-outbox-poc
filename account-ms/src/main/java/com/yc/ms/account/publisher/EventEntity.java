package com.yc.ms.account.publisher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

/**
 * Event Table to which the {@link EventWrappedProxy} publishes the event.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event", schema = "account")
@Slf4j
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String data;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}
