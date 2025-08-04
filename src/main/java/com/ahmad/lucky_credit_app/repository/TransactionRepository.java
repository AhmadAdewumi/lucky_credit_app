package com.ahmad.lucky_credit_app.repository;

import com.ahmad.lucky_credit_app.model.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transactions, UUID> {
}
