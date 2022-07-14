package com.yc.ms.account.controller;

import com.yc.ms.account.model.Account;
import com.yc.ms.account.model.AccountCreateRequestDto;
import com.yc.ms.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreateRequestDto request) {
        return ResponseEntity.ok(accountService.createAccount(
                Account.builder()
                        .accountNumber(request.getAccountNumber())
                        .initialDeeposit(request.getInitialDeposit())
                        .customerId(request.getCustomerId())
                        .accountType(request.getAccountType())
                        .build()
        ));
    }
}
