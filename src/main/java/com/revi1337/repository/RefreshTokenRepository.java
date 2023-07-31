package com.revi1337.repository;

import com.revi1337.domain.RefreshToken;
import com.revi1337.repository.querydsl.RefreshTokenQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenQueryDslRepository {

//    @Query(value = "select t from RefreshToken as t inner join t.userAccount as u where u.email = :email")
//    Optional<RefreshToken> findRelatedRefreshToken(@Param("email") String email);

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update RefreshToken as r set r.token = :token")
    int updateToken(@Param("token") String token);

}
