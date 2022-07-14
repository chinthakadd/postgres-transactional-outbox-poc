package com.yc.ms.account.model;

import com.yc.ms.account.model.enums.AccountStatus;
import com.yc.ms.account.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String accountId;
    private String accountNumber;
    private String customerId;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private BigDecimal initialDeeposit;
    private Instant createdAt;
    private String createdBy;

}
