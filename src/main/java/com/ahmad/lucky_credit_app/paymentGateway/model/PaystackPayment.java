package com.ahmad.lucky_credit_app.paymentGateway.model;

import com.ahmad.lucky_credit_app.model.Users;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paystack_payment", schema = "lucky_credit_app")
public class PaystackPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private String reference;

    private BigDecimal amount;

    private String gatewayResponse;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;

    private LocalDateTime createdOn;
}
