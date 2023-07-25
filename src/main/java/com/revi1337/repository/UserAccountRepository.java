package com.revi1337.repository;


import com.revi1337.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

    Optional<UserAccount> findByEmail(String email);
}
