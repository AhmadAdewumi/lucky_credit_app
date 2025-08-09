package com.ahmad.lucky_credit_app.repository;

import com.ahmad.lucky_credit_app.model.Users;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<Users, UUID> {
    Optional<Users> findByUsername(@NotNull String username);

    @Query(value = """
            SELECT *,
                   (CASE WHEN user_gender = :gender THEN 1 ELSE 0 END) +
                   (CASE WHEN skin_color = :color THEN 1 ELSE 0 END) +
                   (CASE WHEN marital_status = :marital_status THEN 1 ELSE 0 END) +
                   (CASE WHEN occupation ILIKE :occupation THEN 1 ELSE 0 END) +
                   (CASE WHEN nationality ILIKE :nationality THEN 1 ELSE 0 END) AS match_score
            FROM lucky_credit_app.users
            WHERE (:min_height IS NULL OR height_in_cm >= :min_height)
              AND (:max_height IS NULL OR height_in_cm <= :max_height)
              AND (:min_family_size IS NULL OR family_size >= :min_family_size)
              AND (:max_family_size IS NULL OR family_size <= :max_family_size)
              AND (:min_siblings_count IS NULL OR siblings_count >= :min_siblings_count)
              AND (:max_siblings_count IS NULL OR siblings_count <= :max_siblings_count)
            ORDER BY match_score DESC, times_drawn ASC, last_draw_date ASC
            LIMIT :user_count + 15;
            """, nativeQuery = true)
    List<Users> findMatchingUsers(
            @Param("gender") String gender,
            @Param("color") String color,
            @Param("marital_status") String marital_status,
            @Param("occupation") String occupation,
            @Param("nationality") String nationality,
            @Param("min_height") BigDecimal min_height,
            @Param("max_height") BigDecimal max_height,
            @Param("min_family_size") Integer min_family_size,
            @Param("max_family_size") Integer max_family_size,
            @Param("min_siblings_count") Integer min_siblings_count,
            @Param("max_siblings_count") Integer max_siblings_count,
            @Param("user_count") int user_count
    );

    boolean existsByEmail(@NotNull String email);

    Optional<Users> findByEmail(@NotNull String email);
}

//SELECT *,
//        (CASE WHEN user_gender = :gender THEN 1 ELSE 0 END) +
//        (CASE WHEN skin_color = :color THEN 1 ELSE 0 END) +
//        (CASE WHEN marital_status = :marital_status THEN 1 ELSE 0 END) +
//        (CASE WHEN occupation ILIKE :occupation THEN 1 ELSE 0 END) +
//        (CASE WHEN nationality ILIKE :nationality THEN 1 ELSE 0 END) AS match_score
//FROM users
//WHERE (:min_height IS NULL OR height_in_cm >= :min_height)
//AND (:max_height IS NULL OR height_in_cm <= :max_height)
//AND (:min_family_size IS NULL OR family_size >= :min_family_size)
//AND (:max_family_size IS NULL OR family_size <= :max_family_size)
//AND (:min_siblings_count IS NULL OR siblings_count >= :min_siblings_count)
//AND (:max_siblings_count IS NULL OR siblings_count <= :max_siblings_count)
//ORDER BY match_score DESC, times_drawn ASC, last_draw_time ASC
//LIMIT :user_count + 15;


