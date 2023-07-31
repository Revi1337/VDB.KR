package com.revi1337.repository;

import com.revi1337.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {

    Optional<EmailToken> findByToken(String token);

    @Modifying
    @Query(value = "update EmailToken as t set t.confirmedAt = :confirmedAt where t.token = :token")
    int updateEmailTokenConfirm(@Param("token") String token, @Param("confirmedAt") LocalDateTime confirmedAt);

}
