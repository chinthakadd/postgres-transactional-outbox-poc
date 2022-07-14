package com.yc.ms.account.integration.client;


import com.yc.ms.account.model.Account;

public interface AccountRepositoryClient {
    Account saveAccount(Account account);
}
