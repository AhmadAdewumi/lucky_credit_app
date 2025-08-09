package com.ahmad.lucky_credit_app.service.transaction;

import com.ahmad.lucky_credit_app.dto.request.CreateTransactionRequest;
import com.ahmad.lucky_credit_app.enums.TransStatus;
import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.ResourceNotFoundException;
import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.InsufficientFundException;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.repository.AccountRepository;
import com.ahmad.lucky_credit_app.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }


    //get the accounts using the UUID
    //get the source and destination account number
    //
    @Transactional
    @Override
    public Transactions initiateTransaction(CreateTransactionRequest request) {
        // get the source account
        Account sourceAccount = accountRepository.findById(request.getSourceAccountId().getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with ID: %s NOT FOUND!", request.getSourceAccountId().getId())));

        //get the destination account
        Account destinationAccount = accountRepository.findById(request.getDestinationAccountId().getId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with ID: %s NOT FOUND!", request.getDestinationAccountId().getId())));

        // get the balance of the source account
        BigDecimal getSourceAccountBalance = sourceAccount.getBalance();
        // get the destination account balance, if null as i am not sure i set default in the db, set to ZERO
        BigDecimal getDestinationAccountBalance = destinationAccount.getBalance() != null ? destinationAccount.getBalance() : BigDecimal.ZERO;
        //get the amount to transfer
        BigDecimal amountToTransfer = request.getAmount();

        // check if the source account initial balance is less than the amount he wants to transfer
        if (getSourceAccountBalance == null || amountToTransfer.compareTo(getSourceAccountBalance) > 0) {
            throw new InsufficientFundException("You have insufficient amount in your account!");
        }

        // debit the source account
        sourceAccount.setBalance(getSourceAccountBalance.subtract(amountToTransfer));
        // credit the destination account
        destinationAccount.setBalance(getDestinationAccountBalance.add(amountToTransfer));

        Transactions transaction = new Transactions();
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setNote(request.getNote());
        transaction.setTransactionStatus(TransStatus.SUCCESS);
        transaction.setReference("TXN-"+ UUID.randomUUID().toString().replace("-", "").toUpperCase());
        transaction.setCreatedAt(LocalDateTime.now());


        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        transactionRepository.save(transaction);

        return transaction;
    }
}
