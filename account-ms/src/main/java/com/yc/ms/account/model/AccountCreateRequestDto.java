package com.yc.ms.account.model;

import com.yc.ms.account.model.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequestDto {
    private String accountNumber;
    private String customerId;
    private AccountType accountType;
    private BigDecimal initialDeposit;
}
