package com.ahmad.lucky_credit_app.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiErrorResponse {
    private LocalDateTime timeStamp;
    private String message;
    private String path;
    private int statusCode;
    private String error;
}
