package com.ahmad.lucky_credit_app.dto.request;

import com.ahmad.lucky_credit_app.enums.AccStatus;
import com.ahmad.lucky_credit_app.enums.AccType;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.model.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountRequest {
    private AccType accountType;

    private BigDecimal balance;

    private Users user;

    private AccStatus accountStatus;

//    @OneToMany(mappedBy = "sourceAccount")
//    private List<Transactions> transactions = new ArrayList<>();

    private LocalDateTime createdAt;
}
