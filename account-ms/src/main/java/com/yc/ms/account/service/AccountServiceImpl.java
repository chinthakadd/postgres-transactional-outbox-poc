package com.yc.ms.account.service;

import com.yc.ms.account.integration.client.AccountRepositoryClient;
import com.yc.ms.account.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepositoryClient accountRepositoryClient;

    @Override
    public Account createAccount(Account account) {
        return accountRepositoryClient.saveAccount(account);
    }
}
