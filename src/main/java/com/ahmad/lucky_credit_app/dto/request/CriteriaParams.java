package com.ahmad.lucky_credit_app.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaParams {
    private String gender;
    private String color;
    private String occupation;
    private String nationality;
    private String maritalStatus;
    private BigDecimal minHeight;
    private BigDecimal maxHeight;
    private Integer minFamilySize;
    private Integer maxFamilySize;
    private Integer minSiblingsCount;
    private Integer maxSiblingsCount;
    private int userCount;
}
