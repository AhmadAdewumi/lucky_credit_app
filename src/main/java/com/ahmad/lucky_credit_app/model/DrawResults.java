package com.ahmad.lucky_credit_app.model;

import com.ahmad.lucky_credit_app.enums.DrawResultStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "draw_results")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawResults {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "winner_acc_number")
    private Account winnerAccountNumber;

    private BigDecimal AmountTransferred;

    private String drawReference;

    @Enumerated(EnumType.STRING)
    private DrawResultStatus status;

    @ManyToOne
    @JoinColumn(name = "triggered_by")
    private Users triggeredBy;

    private LocalDateTime createdAt;
}
