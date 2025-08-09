package com.ahmad.lucky_credit_app.service.transaction;

import com.ahmad.lucky_credit_app.dto.request.CreateTransactionRequest;
import com.ahmad.lucky_credit_app.enums.TransStatus;
import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.InsufficientFundException;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.repository.AccountRepository;
import com.ahmad.lucky_credit_app.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

//    UUID testUUID = UUID.randomUUID();

    @Test
    void initiateTransaction() {
        Account sourceAccount = new Account();
        UUID sourceAccId = UUID.randomUUID();
        sourceAccount.setId(sourceAccId);
        sourceAccount.setBalance(BigDecimal.valueOf(5000));

        Account destinationAccount = new Account();
        UUID destAccId = UUID.randomUUID();
        destinationAccount.setId(destAccId);
        destinationAccount.setBalance(BigDecimal.valueOf(2000));

        when(accountRepository.findById(sourceAccId)).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(destAccId)).thenReturn(Optional.of(destinationAccount));

        CreateTransactionRequest request = new CreateTransactionRequest(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(2000),
                TransStatus.SUCCESS,
                sourceAccount,
                destinationAccount,
                "Sadaqah",
                LocalDateTime.now()
        );


//        BigDecimal amountToTransfer

        Transactions transaction = new Transactions();
        transaction.setAmount(request.getAmount());
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setNote("Sadaqah");

        when(transactionRepository.save(any(Transactions.class))).thenReturn(transaction);

        //Act
        Transactions result = transactionService.initiateTransaction(request);

        //Assert
        ArgumentCaptor<Transactions> transactionCaptor = ArgumentCaptor.forClass(Transactions.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Transactions capturedTransaction = transactionCaptor.getValue();
        assertThat(capturedTransaction.getAmount()).isEqualTo(request.getAmount());
        assertThat(capturedTransaction.getSourceAccount()).isEqualTo(request.getSourceAccountId());
        assertThat(capturedTransaction.getDestinationAccount()).isEqualTo(request.getDestinationAccountId());

        assertThat(result.getSourceAccount().getBalance()).isEqualTo(BigDecimal.valueOf(3000));
        assertThat(result.getDestinationAccount().getBalance()).isEqualTo(BigDecimal.valueOf(4000));
    }

    @Test
    void throwExceptionWhenInsufficientFunds() {
        Account sourceAccount = new Account();
        sourceAccount.setId(UUID.randomUUID());
        sourceAccount.setBalance(BigDecimal.valueOf(500));

        Account detinationAccount = new Account();
        detinationAccount.setId(UUID.randomUUID());
        detinationAccount.setBalance(BigDecimal.valueOf(0));

        when(accountRepository.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findById(detinationAccount.getId())).thenReturn(Optional.of(detinationAccount));

        CreateTransactionRequest request = new CreateTransactionRequest(
                UUID.randomUUID().toString(),
                BigDecimal.valueOf(2000),
                TransStatus.SUCCESS,
                sourceAccount,
                detinationAccount,
                "Sadaqah",
                LocalDateTime.now()
        );

        assertThatThrownBy(
                () -> transactionService.initiateTransaction(request))
                .isInstanceOf(InsufficientFundException.class)
                .hasMessageContaining("insufficient amount");
    }
}