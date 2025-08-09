package com.ahmad.lucky_credit_app.model;

import com.ahmad.lucky_credit_app.enums.AccStatus;
import com.ahmad.lucky_credit_app.enums.AccType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "accounts", schema = "lucky_credit_app")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccType accountType;

    @Enumerated(EnumType.STRING)
    private AccStatus accountStatus;

    @Column(name = "account_balance")
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @OneToMany(mappedBy = "sourceAccount")
    private List<Transactions> transactions = new ArrayList<>();

    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id) && Objects.equals(accountNumber, account.accountNumber) && accountType == account.accountType && accountStatus == account.accountStatus && Objects.equals(balance, account.balance) && Objects.equals(user, account.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountNumber, accountType, accountStatus, balance, user);
    }
}
