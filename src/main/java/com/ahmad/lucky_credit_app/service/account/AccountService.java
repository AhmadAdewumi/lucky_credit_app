package com.ahmad.lucky_credit_app.service.account;

import com.ahmad.lucky_credit_app.dto.request.CreateAccountRequest;
import com.ahmad.lucky_credit_app.globalExceptionHandling.ResourceNotFoundException;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.AccountRepository;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    @Override
    public Account createAccountForUser(UUID userId, CreateAccountRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID: %s Not Found!", userId)));

        Account account = new Account();
        account.setAccountNumber(generateAccountNumber());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCreatedAt(LocalDateTime.now());
        account.setUser(user);
        account.setCreatedAt(LocalDateTime.now());

        accountRepository.save(account);
        return account;
    }

    @Override
    public Account getAccountByUserId(UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID: %s Not Found!", userId)));

        return accountRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with user ID: %s Not Found!", userId)));

    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with account number: %s Not Found!", accountNumber)));
    }

    @Override
    public List<Account> listAllAccounts() {
        return accountRepository.findAll();
    }
}
