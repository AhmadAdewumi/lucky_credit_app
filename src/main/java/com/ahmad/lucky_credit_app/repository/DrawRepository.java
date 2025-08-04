package com.ahmad.lucky_credit_app.repository;

import com.ahmad.lucky_credit_app.model.DrawResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DrawRepository extends JpaRepository<DrawResults, UUID> {

}
