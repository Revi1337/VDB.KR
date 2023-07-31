//package com.revi1337.repository;
//
//
//import com.revi1337.QueryDslTestConfig;
//import com.revi1337.config.domain.JPAConfig;
//import com.revi1337.domain.UserAccount;
//import com.revi1337.domain.RefreshToken;
//import com.revi1337.dto.UserAccountDto;
//import com.revi1337.dto.security.AuthenticatedUserAccount;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.repository.query.Param;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.List;
//
//@Import({JPAConfig.class, QueryDslTestConfig.class})
//@DataJpaTest(showSql = false) @Rollback(false)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class RefreshTokenRepositoryTest {
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    private final UserAccountRepository userAccountRepository;
//
//    @PersistenceContext EntityManager entityManager;
//
//    @Autowired
//    public RefreshTokenRepositoryTest(RefreshTokenRepository refreshTokenRepository,
//                                      UserAccountRepository userAccountRepository) {
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.userAccountRepository = userAccountRepository;
//    }
//
//    @BeforeEach
//    public void initDataBase() {
//        System.out.println("===========INIT===========");
//        UserAccount userAccount1 = createUserAccount("revi1331@naver.com", "revi1337", "1337");
//        UserAccount userAccount2 = createUserAccount("revi1332@naver.com", "revi1337", "1337");
//        UserAccount userAccount3 = createUserAccount("revi1333@naver.com", "revi1337", "1337");
//        userAccountRepository.saveAll(List.of(userAccount1, userAccount2, userAccount3));
//
//        RefreshToken refreshToken1 = createRefreshToken(userAccount1);
//        RefreshToken refreshToken2 = createRefreshToken(userAccount2);
//        RefreshToken refreshToken3 = createRefreshToken(userAccount3);
//        refreshTokenRepository.saveAll(List.of(refreshToken1, refreshToken2, refreshToken3));
//        entityManager.flush();
//        entityManager.clear();
//    }
//
//    @AfterEach
//    public void removeAll() {
//        System.out.println("===========DELETE===========");
//        refreshTokenRepository.deleteAll();
//        userAccountRepository.deleteAll();
//    }
//
//    @DisplayName(value = "[Repository : Email 과 관련된 RefreshToken 조회 (JPA)]")
//    @Test
//    public void findTokenReferenceByUserAccountUsingJPA() {
//        System.out.println("=======================");
//        RefreshToken refreshToken = entityManager.createQuery(
//                        "select t from RefreshToken as t inner join t.userAccount as u where u.email = :email", RefreshToken.class)
//                .setParameter("email", "revi1333@naver.com")
//                .getSingleResult();
//        System.out.println("=======================");
//        System.out.println(refreshToken);
//        System.out.println("=======================");
//    }
//
//    @DisplayName(value = "[Repository : Email 과 관련된 RefreshToken 조회 (DataJPA)]")
//    @Test
//    public void findTokenReferenceByUserAccountUsingDataJPA() {
//        System.out.println("=======================");
//        RefreshToken refreshToken = refreshTokenRepository
//                .findRelatedRefreshToken("revi1333@naver.com").orElseThrow(IllegalArgumentException::new);
//        System.out.println("=======================");
//        System.out.println(refreshToken);
//        System.out.println("=======================");
//    }
//
//    @DisplayName(value = "[Repository : Email 과 관련된 RefreshToken 조회 (QueryDsl)]")
//    @Test
//    public void findTokenReferenceByUserAccountUsingQueryDsl() {
//        System.out.println("=======================");
//        RefreshToken refreshToken = refreshTokenRepository
//                .findRelatedRefreshToken2("revi1333@naver.com");
//        System.out.println("=======================");
//        System.out.println(refreshToken);
//        System.out.println("=======================");
//    }
//
//    @DisplayName(value = "")
//    @Test
//    public void findTokenReferenceByUserAccountUsingQueryDsl2() {
//        System.out.println("=======================");
////        UserAccount userAccount = userAccountRepository.findByEmail("revi1333@naver.com").orElse(null);
//
////        RefreshToken refreshToken = refreshTokenRepository.findRelatedRefreshToken("revi1333@naver.com").orElse(null);
//
////        UserAccount userAccount = userAccountRepository.findRefreshTokenByUserAccountEmail("revi1333@naver.com").orElse(null);
//
//        System.out.println("=======================");
////        System.out.println("userAccount = " + userAccount.getRefreshToken().getToken());
//        System.out.println("=======================");
//    }
//
//
//    private UserAccount createUserAccount(String email, String username, String password) {
//        return UserAccount.create()
//                .email(email)
//                .username(username)
//                .password(password)
//                .build();
//    }
//
//    private RefreshToken createRefreshToken(UserAccount userAccount) {
//        return RefreshToken.create()
//                .remoteIpAddress("0:0:0:0:0:0:1")
//                .loggedIn(false)
//                .token("dummyToken")
//                .userAccount(userAccount)
//                .build();
//    }
//
//}
