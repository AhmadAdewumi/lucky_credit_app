package com.ahmad.lucky_credit_app.paymentGateway.service;

import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.PaystackInitializationException;
import com.ahmad.lucky_credit_app.globalExceptionHandling.exceptions.ResourceNotFoundException;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.paymentGateway.constants.APIConstants;
import com.ahmad.lucky_credit_app.paymentGateway.dto.request.InitializePaymentRequest;
import com.ahmad.lucky_credit_app.paymentGateway.dto.response.InitializePaymentResponse;
import com.ahmad.lucky_credit_app.paymentGateway.dto.response.PaymentVerificationResponse;
import com.ahmad.lucky_credit_app.paymentGateway.model.PaystackPayment;
import com.ahmad.lucky_credit_app.paymentGateway.repository.PaystackRepository;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaystackService implements IPaystackService {
    private final RestTemplate restTemplate;
    private final PaystackRepository paystackRepository;
    private final UserRepository userRepository;

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;
    private String paystackInitializationUrl = APIConstants.PAYSTACK_INITIALIZE_PAY;
    private String paystackVerifyUrl = APIConstants.PAYSTACK_VERIFY;

    public PaystackService(RestTemplate restTemplate, PaystackRepository paystackRepository, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.paystackRepository = paystackRepository;
        this.userRepository = userRepository;
    }

    @Override
    public InitializePaymentResponse initializePayment(InitializePaymentRequest paymentRequest) {
        InitializePaymentRequest request = new InitializePaymentRequest();
        request.setEmail(paymentRequest.getEmail());
        //to convert the amount to kobo
        request.setAmount(paymentRequest.getAmount().multiply(BigDecimal.valueOf(100)));
        request.setReference(UUID.randomUUID().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(paystackSecretKey);

        HttpEntity<InitializePaymentRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<InitializePaymentResponse> response = restTemplate.postForEntity(
                paystackInitializationUrl,
                entity,
                InitializePaymentResponse.class
        );

        if (response.getBody() == null || Boolean.FALSE.equals(response.getBody().getStatus())) {
            throw new PaystackInitializationException("Paystack initialize payment failed");
        }

        return response.getBody();
    }

    @Override
    @Transactional
    public PaymentVerificationResponse paymentVerification(String reference, String email) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecretKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<PaymentVerificationResponse> response = restTemplate.exchange(
                paystackVerifyUrl + reference,
                HttpMethod.GET,
                entity,
                PaymentVerificationResponse.class
        );

        PaymentVerificationResponse body = response.getBody();

        if (body == null || Boolean.FALSE.equals(response.getBody().getStatus())) {
            throw new PaystackInitializationException("Paystack initialize payment failed");
        }

        PaymentVerificationResponse.Data data = body.getData();
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with email: %s, Not Found!", email)));

        PaystackPayment payment = PaystackPayment.builder()
                .user(user)
                .reference(data.getReference())
                .amount(data.getAmount())
                .gatewayResponse(data.getGatewayResponse())
                .paidAt(data.getPaidAt())
                .createdAt(data.getCreatedAt())
                .createdOn(LocalDateTime.now())
                .build();

        paystackRepository.save(payment);

        return body;
    }
}
