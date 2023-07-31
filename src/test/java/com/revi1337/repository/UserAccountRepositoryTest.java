package com.revi1337.repository;

import com.revi1337.QueryDslTestConfig;
import com.revi1337.config.domain.JPAConfig;
import com.revi1337.domain.RefreshToken;
import com.revi1337.domain.UserAccount;
import com.revi1337.domain.enumerate.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName(value = "[Domain : Domain Slice Test]")
@Import({JPAConfig.class, QueryDslTestConfig.class}) @DataJpaTest(showSql = false) @Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserAccountRepositoryTest {

    private final UserAccountRepository userAccountRepository;

    @PersistenceContext EntityManager entityManager;

    @Autowired
    public UserAccountRepositoryTest(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @BeforeEach
    public void beforeEach() {
        userAccountRepository.deleteAll();
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

    @Test
    @DisplayName(value = "[DOMAIN : 전체회원 조회 테스트]")
    public void findAllUsersTest() {
        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("revi1337")
                .password("1337")
                .build();
        userAccountRepository.save(userAccount);

        List<UserAccount> userAccounts = userAccountRepository.findAll();
        assertThat(userAccounts).size().isEqualTo(1);
    }

    @Test
    @DisplayName(value = "[DOMAIN : RefreshToken 조회 테스트]")
    public void findRefreshTokenTest() {
        RefreshToken refreshToken = RefreshToken
                .create()
                .token("dummyToken")
                .remoteIpAddress("asd")
                .loggedIn(false)
                .build();

        UserAccount userAccount = UserAccount.create()
                .email("revi1337@naver.com")
                .username("revi1337")
                .password("1337")
                .refreshToken(refreshToken)
                .role(Role.USER)
                .build();
        userAccountRepository.save(userAccount);

        entityManager.flush();
        entityManager.clear();

        System.out.println("====== EXECUTE ======");
        UserAccount findUserAccount = userAccountRepository.findRefreshTokenByUserAccountEmail(userAccount.getEmail())
                .orElseThrow(IllegalArgumentException::new);
        System.out.println("======================");
        System.out.println("====== SOUT ======");
        System.out.println("findUserAccount.getRefreshToken() = " + findUserAccount.getId());
        System.out.println("======================");
    }

}