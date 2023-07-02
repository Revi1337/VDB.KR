package com.revi1337.domain;

import com.revi1337.config.domain.JPAConfig;
import com.revi1337.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;


@DisplayName(value = "[Domain : Domain Slice Test]")
@Import(JPAConfig.class) @DataJpaTest(showSql = false) @Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountRepositoryTest(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Test
    @DisplayName(value = "[DOMAIN : 회원가입 테스트]")
    public void joinUserTest() {
        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("revi1337")
                .password("1337")
                .build();
        userAccountRepository.save(userAccount);
    }

}