package com.ahmad.lucky_credit_app.service.users;

import com.ahmad.lucky_credit_app.dto.request.CreateUserRequest;
import com.ahmad.lucky_credit_app.globalExceptionHandling.ResourceNotFoundException;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Users registerUser(CreateUserRequest request) {
        log.info("Attempting to create User with attributes {}", request.toString());
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setDescription(request.getDescription());
        user.setGender(request.getGender());
        user.setNationality(request.getNationality());
        user.setFamilySize(request.getFamilySize());
        user.setOccupation(request.getOccupation());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRealName(request.getRealName());
        user.setMaritalStatus(request.getMaritalStatus());
        user.setHeight_in_cm(request.getHeight_in_cm());
        user.setSiblingsCount(request.getSiblingsCount());
        user.setSkinColor(request.getSkinColor());

        userRepository.save(user);
        log.info("Useer created Successfully!");

        return user;
    }

    public Users updateUserInfo(){
        return null;
    }

    @Override
    public Users getUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID: %s Not Found!", userId)));
    }

    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("User with ID: %s Not Found!", username)));
    }
}
