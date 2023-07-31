package com.revi1337.service;

import com.revi1337.domain.EmailToken;
import com.revi1337.domain.UserAccount;
import com.revi1337.domain.RefreshToken;
import com.revi1337.domain.enumerate.Role;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.exception.DuplicateEmailException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Component Service
 */
@Service @RequiredArgsConstructor @Transactional
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JWTService jwtService;

    private final EmailSendService emailSendService;

    private final EmailTokenService emailTokenService;

    private final UserAccountService userAccountService;

    public void register(UserAccountDto userAccountDto, HttpServletRequest httpServletRequest) {
        if (userAccountService.findUserAccountByEmail(userAccountDto.email()).isPresent())
            throw new DuplicateEmailException();

        // Generate Refresh Token
        String refToken = jwtService.generateRefreshToken(userAccountDto.email());
        RefreshToken refreshToken = RefreshToken.create()
                .token(refToken)
                .remoteIpAddress(httpServletRequest.getRemoteAddr())
                .loggedIn(false)
                .build();

        // Encode Password & Save UserAccount
        UserAccount userAccount = userAccountDto.toEntity(refreshToken);
        userAccount.changePassword(passwordEncoder.encode(userAccountDto.password()));
        userAccountService.saveUserAccount(userAccount);

        // Generate EmailToken & SaveToken & Send Email
        String randomConfirmToken = UUID.randomUUID().toString();
        EmailToken emailToken = EmailToken.create()
                .token(randomConfirmToken)
                .userAccount(userAccount)
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        emailTokenService.saveEmailToken(emailToken);
        String activateLink = "http://localhost:8080/api/v1/auth/register/confirm?token=" + randomConfirmToken;
        emailSendService.send(userAccountDto.username(), userAccount.getEmail(), "Confirm your email", activateLink);
    }

    public void confirmToken(String token) {
        EmailToken emailToken = emailTokenService.getEmailToken(token);
        emailToken.confirmEmailToken(emailToken.getToken(), LocalDateTime.now());
//        emailTokenService.confirmToken(emailToken.getToken(), LocalDateTime.now());   // TODO Bulk 연산으로 쿼리를 바로 보내던가 아니면, 비지니스 로직으로 할지 고민좀 해봐야한다.

        UserAccount userAccount = emailToken.getUserAccount();
        userAccount.activateUserAccount(true, Role.USER);
//        userAccountService.enableUserAccount(userAccount.getEmail());                 // TODO Bulk 연산으로 쿼리를 바로 보내던가 아니면, 비지니스 로직으로 할지 고민좀 해봐야한다.
    }

//    @PersistenceContext private EntityManager entityManager;
//    Set<ManagedType<?>> managedTypes = entityManager.getMetamodel().getManagedTypes();
//        System.out.println("managedTypes = " + managedTypes);
}
