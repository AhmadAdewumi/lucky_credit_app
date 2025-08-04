package com.ahmad.lucky_credit_app.repository;

import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUser(Users user);

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUserId(UUID userId);
}
