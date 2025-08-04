package com.ahmad.lucky_credit_app.service.transaction;

import com.ahmad.lucky_credit_app.dto.request.CreateTransactionRequest;
import com.ahmad.lucky_credit_app.model.Transactions;

public interface ITransactionService {
    Transactions initiateTransaction(CreateTransactionRequest transactions);
}
