package com.yc.ms.account.integration.model.entity;

import com.yc.ms.account.model.enums.AccountStatus;
import com.yc.ms.account.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "account")
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
}
