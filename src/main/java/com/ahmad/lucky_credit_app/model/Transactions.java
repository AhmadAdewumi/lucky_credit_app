package com.ahmad.lucky_credit_app.model;

import com.ahmad.lucky_credit_app.enums.TransStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions",schema = "lucky_credit_app")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String reference;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransStatus transactionStatus;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    @JsonIgnore
    private Account sourceAccount;

    @ManyToOne
    @JoinColumn(name = "destination_account_id")
//    @JsonIgnore
    private Account destinationAccount;

    private String note;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transactions that = (Transactions) o;
        return Objects.equals(id, that.id) && Objects.equals(reference, that.reference) && Objects.equals(amount, that.amount) && transactionStatus == that.transactionStatus && Objects.equals(sourceAccount, that.sourceAccount) && Objects.equals(destinationAccount, that.destinationAccount) && Objects.equals(note, that.note);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, amount, transactionStatus, sourceAccount, destinationAccount, note);
    }
}
