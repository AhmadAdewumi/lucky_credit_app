package com.ahmad.lucky_credit_app.dto.request;

import com.ahmad.lucky_credit_app.enums.Gender;
import com.ahmad.lucky_credit_app.enums.MarriageStatus;
import com.ahmad.lucky_credit_app.enums.SkinColor;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private UUID uuid;

    private String username;

    private String realName;

    private String email;

    private String phoneNumber;

    private String occupation;

    private Gender gender;

    private String nationality;

    private String description;

    private double height_in_cm;

    private MarriageStatus maritalStatus;

    private int familySize;

    private int siblingsCount;

    private SkinColor skinColor;

    public CreateUserRequest(String username, String realName,
                             String email, String phoneNumber,
                             String occupation, Gender gender,
                             String nationality, String description,
                             double height_in_cm, MarriageStatus maritalStatus,
                             int familySize, int siblingsCount, SkinColor skinColor) {
        this.username = username;
        this.realName = realName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.occupation = occupation;
        this.gender = gender;
        this.nationality = nationality;
        this.description = description;
        this.height_in_cm = height_in_cm;
        this.maritalStatus = maritalStatus;
        this.familySize = familySize;
        this.siblingsCount = siblingsCount;
        this.skinColor = skinColor;
    }

}


