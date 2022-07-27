package com.yc.ms.account.integration.model.entity;

import com.yc.ms.account.model.enums.AccountStatus;
import com.yc.ms.account.model.enums.AccountType;
import com.yc.ms.account.publisher.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Entities Managed by Repositories are Aggregate Roots according to Spring.
 * Refer: https://docs.spring.io/spring-data/commons/docs/current/reference/html/#core.domain-events
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account")
@Slf4j
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountNumber;
    private String customerId;
    private AccountType accountType;
    private AccountStatus accountStatus = AccountStatus.ACTIVE;
    @CreationTimestamp
    private Instant createdAt;

    @DomainEvents
    Collection<Object> domainEvents() {
        log.info("Domain Event Publication");
        return Collections.singleton(new DomainEvent(Map.of("id", id, "accountNumber", accountNumber)));
    }

    @AfterDomainEventPublication
    void callbackMethod() {
        // clean up logic.
        log.info("Cleanup After Domain Event Publication");
    }

}
