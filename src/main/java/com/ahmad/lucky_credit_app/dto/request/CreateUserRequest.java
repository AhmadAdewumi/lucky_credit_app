package com.ahmad.lucky_credit_app.dto.request;

import com.ahmad.lucky_credit_app.enums.Gender;
import com.ahmad.lucky_credit_app.enums.MarriageStatus;
import com.ahmad.lucky_credit_app.enums.SkinColor;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
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
}
