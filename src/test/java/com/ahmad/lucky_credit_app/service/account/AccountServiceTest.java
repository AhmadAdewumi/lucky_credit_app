package com.ahmad.lucky_credit_app.service.account;

import com.ahmad.lucky_credit_app.dto.request.CreateAccountRequest;
import com.ahmad.lucky_credit_app.enums.AccStatus;
import com.ahmad.lucky_credit_app.enums.AccType;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.AccountRepository;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccountForUser() {
        //Arrange
        UUID userId = UUID.randomUUID();
        Users mockUser = new Users();
        mockUser.setId(userId);
        mockUser.setRealName("AhmadAdewumi");

        String accNo = accountService.generateAccountNumber();

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        CreateAccountRequest request = new CreateAccountRequest(
                AccType.SAVINGS,
                BigDecimal.valueOf(50000.123),
                mockUser,
                AccStatus.ACTIVE,
                LocalDateTime.now()
        );

        UUID testId = UUID.randomUUID();

        Account expectedAccount = new Account();
        expectedAccount.setId(testId);
        expectedAccount.setAccountType(request.getAccountType());
        expectedAccount.setBalance(request.getBalance());
        expectedAccount.setCreatedAt(request.getCreatedAt());
        expectedAccount.setUser(mockUser);
        expectedAccount.setAccountNumber(accNo);

        // Make account repository.save() return expectedAccount
        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);

        //Act
        Account result = accountService.createAccountForUser(userId, request);
        result.setId(testId);
        result.setAccountNumber(accNo);
        log.info("Result arguments are : accNo ; {},ID :{}, name:{}", result.getAccountNumber(), result.getId(), result.getUser().getRealName());

        //Assert
        //used to peek into parameters passed into the object ,
        //Before saving ,it peeks into it.
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(accountCaptor.capture());

        Account capturedAccount = accountCaptor.getValue();
        assertThat(capturedAccount.getUser()).isEqualTo(mockUser);
        assertThat(capturedAccount.getBalance()).isEqualTo(request.getBalance());
        assertThat(capturedAccount.getAccountType()).isEqualTo(request.getAccountType());

        log.info("account ID for result is {}", result.getId());
        log.info("Account ID for expectedAccount is {}", expectedAccount.getId());
        log.info("Account number for result is {}", expectedAccount.getAccountNumber());

        //verify the returned account form Create Account Service matches our account to save
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedAccount);
        assertThat(result.getUser().getRealName()).isEqualTo("AhmadAdewumi");
    }

    @Test
    @Disabled
    void getAccountByUserId() {
    }

    @Test
    @Disabled
    void getAccountByAccountNumber() {
    }

    @Test
    @Disabled
    void listAllAccounts() {
    }
}