package com.ahmad.lucky_credit_app.service.drawResult;

import com.ahmad.lucky_credit_app.dto.request.CriteriaParams;
import com.ahmad.lucky_credit_app.model.Transactions;
import com.ahmad.lucky_credit_app.model.Users;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface IDrawService {
    //get the winnersId from Ai
    //credit each winner by looping though their id
    @Transactional
    List<Transactions> giveaway(UUID donorAccountId, BigDecimal amountPerUser, String note, @ModelAttribute CriteriaParams params);

    List<Users> filterUserByCriteria(CriteriaParams params);
}
