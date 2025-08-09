package com.ahmad.lucky_credit_app.controller;

import com.ahmad.lucky_credit_app.dto.request.CreateAccountRequest;
import com.ahmad.lucky_credit_app.dto.request.CreateUserRequest;
import com.ahmad.lucky_credit_app.dto.request.CriteriaParams;
import com.ahmad.lucky_credit_app.dto.response.ApiResponse;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.paymentGateway.dto.response.PaymentVerificationResponse;
import com.ahmad.lucky_credit_app.paymentGateway.service.PaystackService;
import com.ahmad.lucky_credit_app.service.account.AccountService;
import com.ahmad.lucky_credit_app.service.account.IAccountService;
import com.ahmad.lucky_credit_app.service.drawResult.DrawService;
import com.ahmad.lucky_credit_app.service.users.IUserService;
import com.ahmad.lucky_credit_app.service.users.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
public class AppController {
    private final IUserService userService;
    private final IAccountService accountService;
    private final DrawService drawService;
    private final PaystackService paystackService;

    public AppController(UserService userService, AccountService accountService, DrawService drawService, PaystackService paystackService) {
        this.userService = userService;
        this.accountService = accountService;
        this.drawService = drawService;
        this.paystackService = paystackService;
    }

    @PostMapping("/user/create")
    private ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        Users result = userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse("User Created successfully", result));
    }

    @PostMapping("/account/create/{userId}")
    private ResponseEntity<ApiResponse> createAccount(@PathVariable UUID userId, CreateAccountRequest request) {
        Account result = accountService.createAccountForUser(userId, request);
        return ResponseEntity.ok(new ApiResponse(String.format("Account created successfully for user with ID: %d", userId), result));
    }

    @PostMapping("/giveaway")
    private ResponseEntity<ApiResponse> drawGiveaway(@RequestBody CriteriaParams params,
                                                     @RequestParam BigDecimal amountPerUser,
                                                     @RequestParam String note,
                                                     @RequestParam UUID donorAccountId
    ) {
        log.info("In draw giveaway controller endpoint!");
        log.info("CriteriaParams are as follows: {},{},{}", params.getUserCount(), params.getNationality(), params.getOccupation());
        List<Transactions> result = drawService.giveaway(donorAccountId, amountPerUser, note, params);
        return ResponseEntity.ok(new ApiResponse("Giveaway drawn for users successfully", result));
    }

    @PostMapping("/giveaway/paystack")
    private ResponseEntity<ApiResponse> drawGiveawayUsingPaystack(@RequestBody CriteriaParams params,
                                                                  @RequestParam String email,
                                                                  @RequestParam BigDecimal amountPerUser,
                                                                  @RequestParam String note) {
        log.info("In draw giveaway using paystack controller endpoint!");
        log.info("CriteriaParams are as follows: {},{},{},{}", params.getUserCount(), params.getNationality(), params.getOccupation(),params.getGender());
        List<Transactions> result = drawService.giveawayUsingPaystack(email,amountPerUser,note,params);
        return ResponseEntity.ok(new ApiResponse("Giveaway drawn via paystack for users successfully", result));
    }

    @PostMapping("/payment/{reference}/verify")
    private ResponseEntity<ApiResponse> createAccount(@PathVariable String reference, @PathVariable @RequestParam String email) throws Exception {
         PaymentVerificationResponse result = paystackService.paymentVerification(reference, email);
        return ResponseEntity.ok(new ApiResponse(String.format("Transaction verified for user with mail: %s", email), result));
    }
}
