package com.revi1337.repository;


import com.revi1337.domain.RefreshToken;
import com.revi1337.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findByEmail(String email);

    @Query(value = "select u from UserAccount as u inner join u.refreshToken as t where u.email = :email")
    Optional<UserAccount> findRefreshTokenByUserAccountEmail(@Param("email") String email);

    @Modifying
    @Query(value = "update UserAccount as u set u.activate = true where u.email = :email")
    int enableUserAccount(@Param("email") String email);

//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(value = "update UserAccount as u set u.refreshToken = :refreshToken where u.email = :email")
//    int updateRefreshToken(@Param("email") String email, @Param("refreshToken") RefreshToken refreshToken);

}
