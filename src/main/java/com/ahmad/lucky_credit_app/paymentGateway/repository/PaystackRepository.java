package com.ahmad.lucky_credit_app.paymentGateway.repository;

import com.ahmad.lucky_credit_app.paymentGateway.model.PaystackPayment;
import org.hibernate.type.descriptor.converter.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaystackRepository extends JpaRepository<PaystackPayment, Long> {

}
