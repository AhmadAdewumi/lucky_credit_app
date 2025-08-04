package com.ahmad.lucky_credit_app.service.account;

import com.ahmad.lucky_credit_app.dto.request.CreateAccountRequest;
import com.ahmad.lucky_credit_app.model.Account;

import java.util.List;
import java.util.UUID;

public interface IAccountService {
    Account createAccountForUser(UUID userId, CreateAccountRequest request);
    Account getAccountByUserId(UUID userId);
    Account getAccountByAccountNumber(String accountNumber);
    List<Account> listAllAccounts();
}
