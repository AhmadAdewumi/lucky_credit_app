package com.ahmad.lucky_credit_app.service.drawResult;

import com.ahmad.lucky_credit_app.dto.request.CriteriaParams;
import com.ahmad.lucky_credit_app.model.Users;

import java.util.List;

public interface IDrawService {
    List<Users> filterUserByCriteria(CriteriaParams params);
}
