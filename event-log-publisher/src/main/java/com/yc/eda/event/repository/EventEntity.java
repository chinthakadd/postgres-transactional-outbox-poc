package com.yc.eda.event.repository;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "event")
public class EventEntity {
    @Id
    private long id;
    private String data;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    private Instant createdAt;
    private Instant lastUpdatedAt;
}
