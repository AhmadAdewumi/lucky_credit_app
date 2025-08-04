package com.ahmad.lucky_credit_app.model;

import com.ahmad.lucky_credit_app.enums.MarriageStatus;
import com.ahmad.lucky_credit_app.enums.Gender;
import com.ahmad.lucky_credit_app.enums.SkinColor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users", schema = "lucky_credit_app")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    private String username;

    @NotNull
    private String realName;

    @NotNull
    private String email;

    @NotNull
    @Column(name = "phone")
    private String phoneNumber;

    @NotNull
    private String occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    private Gender gender;

    @NotNull
    private String nationality;

    private String description;

    @NotNull
    private double height_in_cm;

    @Enumerated(EnumType.STRING)
    private MarriageStatus maritalStatus;

    @NotNull
    private int familySize;

    @NotNull
    private int siblingsCount;

    private int timesDrawn;

    @Column(name = "last_draw_date")
    private LocalDateTime last_draw_time;

    @Enumerated(EnumType.STRING)
    private SkinColor skinColor;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private Account accountId;
}
