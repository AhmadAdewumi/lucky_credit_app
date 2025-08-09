package com.ahmad.lucky_credit_app.paymentGateway.service;

import com.ahmad.lucky_credit_app.paymentGateway.dto.request.InitializePaymentRequest;
import com.ahmad.lucky_credit_app.paymentGateway.dto.response.InitializePaymentResponse;
import com.ahmad.lucky_credit_app.paymentGateway.dto.response.PaymentVerificationResponse;

public interface IPaystackService {
    InitializePaymentResponse initializePayment(InitializePaymentRequest request);
    PaymentVerificationResponse paymentVerification(String reference, String email) throws Exception;
}
