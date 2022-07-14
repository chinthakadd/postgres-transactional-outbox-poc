package com.yc.ms.account.publisher;

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
@Table(name = "event", schema = "account")
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
