package com.ahmad.lucky_credit_app.service.drawResult;

import com.ahmad.lucky_credit_app.dto.request.CreateTransactionRequest;
import com.ahmad.lucky_credit_app.dto.request.CriteriaParams;
import com.ahmad.lucky_credit_app.globalExceptionHandling.ResourceNotFoundException;
import com.ahmad.lucky_credit_app.model.Account;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.model.Users;
import com.ahmad.lucky_credit_app.repository.AccountRepository;
import com.ahmad.lucky_credit_app.repository.UserRepository;
import com.ahmad.lucky_credit_app.service.transaction.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DrawService implements IDrawService {
    private final ChatModel chatModel;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;

    public DrawService(ChatModel chatModel, UserRepository userRepository, TransactionService transactionService, AccountRepository accountRepository) {
        this.chatModel = chatModel;
        this.userRepository = userRepository;
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    //get the winnersId from Ai
    //credit each winner by looping though their id
    @Transactional
    public List<Transactions> giveaway(UUID donorAccountId, BigDecimal amountPerUser, String note, @ModelAttribute CriteriaParams params) {
        log.info("In giveaway method in the draw service class!");
        List<UUID> getWinnersId;
        getWinnersId = getWinnersIdFromAI(params);
        log.debug("AI returned winners ID: {}", getWinnersId.toString());

        Account donorAccount = accountRepository.findById(donorAccountId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with ID: %s NOT FOUND!", donorAccountId)));

        List<Transactions> allTransaction = new ArrayList<>();

        for (UUID winnerId : getWinnersId) {
            Account winnerAccount = accountRepository.findByUserId(winnerId)
                    .orElseThrow(() -> new ResourceNotFoundException(String.format("Account with ID: %s NOT FOUND!", winnerId)));

            CreateTransactionRequest request = CreateTransactionRequest.builder()
                    .sourceAccountId(donorAccount)
                    .destinationAccountId(winnerAccount)
                    .amount(amountPerUser)
                    .note(note)
                    .build();

            Transactions transaction = transactionService.initiateTransaction(request);
            allTransaction.add(transaction);

            Users winner = winnerAccount.getUser();
            incrementUsersDrawParams(winner);
        }

        return allTransaction;
    }

    private void incrementUsersDrawParams(Users winner){
        winner.setTimesDrawn(winner.getTimesDrawn() + 1);
        winner.setLast_draw_time(LocalDateTime.now());
        userRepository.save(winner);
    }

    public List<UUID> getWinnersIdFromAI(@ModelAttribute CriteriaParams params) {
        log.debug("In get winners ID form Ai method in the draw service class!");
        String prompt = buildPrompt(params);
        String winnersId;
        try {
            winnersId = chatModel.call(prompt);
            if (winnersId.isEmpty()) {
                return new ArrayList<>();
            }
            String cleanResponse = cleanAiResponse(winnersId);
            log.debug("AI returned: {}", cleanResponse);

            //convert the strings to UUID by de serializing
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> ids = objectMapper.readValue(cleanResponse, new TypeReference<>() {
            });
            return ids.stream().map(UUID::fromString).toList();
        } catch (RuntimeException | JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

//        try {
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }

    //get all the users
    //the donator pass the description, the count
    @Override
    public List<Users> filterUserByCriteria(@ModelAttribute CriteriaParams params) {
        log.info("In build user criteria method!");
        return userRepository.findMatchingUsers(
                params.getGender(),
                params.getColor(),
                params.getMaritalStatus(),
                params.getOccupation(),
                params.getNationality(),
                params.getMinHeight(),
                params.getMaxHeight(),
                params.getMinFamilySize(),
                params.getMaxFamilySize(),
                params.getMinSiblingsCount(),
                params.getMaxSiblingsCount(),
                params.getUserCount()
        );
    }

    public String buildPrompt(@ModelAttribute CriteriaParams params) {
        log.info("In build prompt method!");
        List<Users> filteredUsers = filterUserByCriteria(params);
        log.info("Filtered Users are as follows: {}", filteredUsers.toString());
        StringBuilder prompt = new StringBuilder();
        int numberOfUsersToCredit = params.getUserCount();
        log.info("Number of users to raffle is: {}", params.getUserCount());

        prompt.append(String.format("""
                You are selecting users to receive a reward based only on their background description.
                
                Below is a list of users. For each, you'll see an ID, real name, and description.
                
                Choose only %s users who are most suitable to receive credit. Use only the 'description' field to decide.
                
                Return ONLY a JSON array of UUIDs, like this: ["uuid1", "uuid2", "uuid3"]. No explanation, no formatting, no changing of the UUIDs.
                
                Users:
                """, numberOfUsersToCredit));
        for (Users user : filteredUsers) {
            prompt.append("{")
                    .append("\"id\":\"").append(user.getId()).append("\",")
                    .append("\"real_name\":\"").append(user.getRealName()).append("\",")
                    .append("\"description\":\"").append(user.getDescription()).append("\"")
                    .append("}\n");
        }

        log.info("Prompt built successfully with content: {}", prompt);
        return prompt.toString();
    }

    private String cleanAiResponse(String aiResponse){
        String cleanedResponse = aiResponse
                .replaceAll("`", "")
                .replaceAll("(?s)^.*?(\\[)", "$1")
                .replaceAll("[\\r\\n]", "")
                .trim();
        log.info("AI Response cleaned to: {}", cleanedResponse);
        return cleanedResponse;

    }
}
