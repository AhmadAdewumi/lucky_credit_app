package com.ahmad.lucky_credit_app.service.users;

import com.ahmad.lucky_credit_app.dto.request.CreateUserRequest;
import com.ahmad.lucky_credit_app.model.Users;

import java.util.UUID;

public interface IUserService {
    Users registerUser(CreateUserRequest request);
    Users getUserById(UUID userId);
    Users getUserByUsername(String username);
}
