package com.ahmad.lucky_credit_app.service.users;

import com.ahmad.lucky_credit_app.dto.request.CreateUserRequest;
import com.ahmad.lucky_credit_app.enums.Gender;
import com.ahmad.lucky_credit_app.enums.MarriageStatus;
import com.ahmad.lucky_credit_app.enums.SkinColor;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Running test for create new user")
    void registerUser() {
        //Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Ahmad",
                "AhmadAdewumi",
                "ahmadadewumi@gmail.com",
                "08162810895",
                "Engineer",
                Gender.MALE,
                "Nigerian",
                "A small Guy seeking knowledge",
                160.74,
                MarriageStatus.SINGLE,
                76,
                12,
                SkinColor.BLACK
        );

        //Act
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        Users expectedResult  = userService.registerUser(request);

        //Assert
        assertThat(expectedResult.getGender()).isEqualTo(request.getGender());
        verify(userRepository).save(any(Users.class));
    }

    @Test
    @Disabled
    void updateUserInfo() {
    }

    @Test
    @Disabled
    void getUserById() {
    }

    @Test
    @Disabled
    void getUserByUsername() {
    }
}