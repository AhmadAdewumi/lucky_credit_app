package com.ahmad.lucky_credit_app.dto.request;

import com.ahmad.lucky_credit_app.enums.TransStatus;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Transactions;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTransactionRequest {
    private String reference;
    private BigDecimal amount;
    private TransStatus transactionStatus;
    private Account sourceAccountId;
    private Account destinationAccountId;
    private String note;
    private LocalDateTime createdAt;
}
