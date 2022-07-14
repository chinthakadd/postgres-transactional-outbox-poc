package com.yc.ms.account.integration.client;

import com.yc.ms.account.integration.model.entity.AccountEntity;
import com.yc.ms.account.integration.repository.AccountRepository;
import com.yc.ms.account.model.Account;
import com.yc.ms.account.model.enums.AccountStatus;
import com.yc.ms.account.publisher.EventWrappedProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRepositoryClientImpl implements AccountRepositoryClient {

    private AccountRepository accountRepository;
    private EventWrappedProxy proxy;

    @Autowired
    public AccountRepositoryClientImpl(AccountRepository accountRepository, EventWrappedProxy proxy) {
        this.accountRepository = accountRepository;
        this.proxy = proxy;
    }

    @Override
    public Account saveAccount(Account account) {
        return proxy.execute(() -> {
            AccountEntity persisted = accountRepository.save(AccountEntity.builder()
                    .accountNumber(account.getAccountNumber())
                    .accountStatus(AccountStatus.ACTIVE)
                    .accountType(account.getAccountType())
                    .customerId(account.getCustomerId())
                    .build());
            account.setAccountId(String.valueOf(persisted.getId()));
            account.setCreatedAt(persisted.getCreatedAt());
            return account;
        });
    }
}
