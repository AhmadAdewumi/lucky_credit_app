package com.ahmad.lucky_credit_app.dataLoader;

import com.ahmad.lucky_credit_app.dto.request.CreateAccountRequest;
import com.ahmad.lucky_credit_app.enums.*;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import com.ahmad.lucky_credit_app.service.account.AccountService;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

//@Component
@Slf4j
public class DataLoader implements CommandLineRunner {
    private final Faker faker = new Faker();
    private final UserRepository userRepository;
    private final AccountService accountService;

    public DataLoader(UserRepository userRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 500; i++) {
            Users user = new Users();
            user.setUsername(limit(faker.name().username()));
            user.setRealName(limit(faker.name().fullName()));
            user.setEmail(limit(faker.internet().emailAddress()));
            user.setPhoneNumber(limit(faker.phoneNumber().cellPhone()));
            user.setOccupation(limit(faker.job().title()));
            user.setGender(Gender.values()[faker.random().nextInt(Gender.values().length)]);
            user.setNationality(limit(faker.country().name()));
            user.setDescription(generateEnglishDescription(faker));
            user.setHeight_in_cm(faker.number().randomDouble(1, 150, 200));
            user.setMaritalStatus(MarriageStatus.values()[faker.random().nextInt(MarriageStatus.values().length)]);
            user.setFamilySize(faker.number().numberBetween(2, 10));
            user.setSkinColor(generateColors());
            user.setSiblingsCount(faker.number().numberBetween(0, 6));
            user.setTimesDrawn(0);
            user.setLast_draw_time(null);

            Users savedUser = userRepository.save(user);

            CreateAccountRequest request = new CreateAccountRequest();
            request.setAccountStatus(AccStatus.ACTIVE);
            request.setAccountType(AccType.SAVINGS);
            request.setBalance(BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 100000)));

            accountService.createAccountForUser(savedUser.getId(), request);

        }
    }

    private String limit(String value) {
        if (value == null) return null;
        return value.length() > 20 ? value.substring(0, 20) : value;
    }

    private SkinColor generateColors() {
        SkinColor[] colors = SkinColor.values();
        Random random = new Random();
        SkinColor randomColors = colors[random.nextInt(colors.length)];
        log.info("Generated random color: {}", randomColors);
        return randomColors;
    }

    private String generateEnglishDescription(Faker faker) {
        String name = faker.name().firstName();
        String occupation = faker.job().title();
        String country = faker.country().name();
        int years = faker.number().numberBetween(2, 20);
        String skill = faker.job().keySkills();
        String goal = faker.company().buzzword();
        String trait = faker.hipster().word();

        return String.format("%s is a %s from %s with over %d years of experience in %s. Known for being %s and passionate about %s, %s is always looking for ways to grow and contribute more.",
                name, occupation, country, years, skill, trait, goal, name);
    }
}

