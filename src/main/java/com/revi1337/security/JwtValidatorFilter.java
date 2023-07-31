package com.revi1337.security;


import com.revi1337.domain.RefreshToken;
import com.revi1337.repository.RefreshTokenRepository;
import com.revi1337.repository.UserAccountRepository;
import com.revi1337.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.HttpHeaders.*;

/**
 * 조건 : 요청 URI 가 /api/v1/auth 가 아니면, 해당필터를 타게되어있음
 *
 * 1. 기본적으로 사용자는 ACCESS_TOKEN 을 달고 요청을 보낸다.
 * 2. REFRESH_TOKEN 을 달고오는 경우는 ACCESS_TOKEN 이 만료되었을때만 보내진다.
 */
@RequiredArgsConstructor @Slf4j @Setter
public class JwtValidatorFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private final JWTService jwtService;

    private final UserAccountRepository userAccountRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private Collection<String> filterExcludePath = Set.of("/api/v1/auth");

    private String reIssuedTokenPath = "/api/reissue/token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (requestURIContainsExcludeURI(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getRequestURI().equals(reIssuedTokenPath)) {

            final String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
            if (refreshToken == null) return;

            String subjectEmail = jwtService.extractClaim(
                    refreshToken, JWTService.TokenType.REFRESH, Claims::getSubject);

            userAccountRepository.findByEmail(subjectEmail)
                    .ifPresent(userAccount -> {
                        String reIssuedRefreshToken = jwtService.generateRefreshToken(subjectEmail);
                        RefreshToken newRefreshToken = RefreshToken.create().token(reIssuedRefreshToken ).build();
                        refreshTokenRepository.updateToken(newRefreshToken.getToken());

                        String reIssuedAccessToken = jwtService.generateAccessToken(subjectEmail);

                        response.setHeader(REFRESH_TOKEN_HEADER, reIssuedAccessToken);
                        response.setHeader(AUTHORIZATION, BEARER + reIssuedAccessToken);
                    });
            return;
        }

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String accessToken = authHeader.substring(7);
        if (jwtService.isAccessTokenValid(accessToken)) {
            log.info("access token valid");
        }
        final String userEmail;
        filterChain.doFilter(request, response);
    }

    private boolean requestURIContainsExcludeURI(HttpServletRequest request) {
        return filterExcludePath.stream()
                .anyMatch(excludePath -> request.getRequestURI().contains(excludePath));
    }

    private Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_HEADER));
    }

}

// RefreshToken 검사 (RefreshToken 이 요청으로 왔다는 것은 RefreshToken 을 재발급해주어야하는 이유밖에 없음.)
// 1. RefreshToken 의 Signature 를 검사해야 한다.
// 2. RefreshToken 이 진짜 만료되었는지 확인해야 한다.
// 3. RefreshToken 을 재발급할떄 AccessToken 또한 재발급해주어야 한다.

//package com.revi1337.security;
//
//
//import com.revi1337.domain.RefreshToken;
//import com.revi1337.repository.RefreshTokenRepository;
//import com.revi1337.repository.UserAccountRepository;
//import com.revi1337.service.JWTService;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Optional;
//import java.util.Set;
//
//import static org.springframework.http.HttpHeaders.*;
//
///**
// * 조건 : 요청 URI 가 /api/v1/auth 가 아니면, 해당필터를 타게되어있음
// *
// * 1. 기본적으로 사용자는 ACCESS_TOKEN 을 달고 요청을 보낸다.
// * 2. REFRESH_TOKEN 을 달고오는 경우는 ACCESS_TOKEN 이 만료되었을때만 보내진다.
// */
//@RequiredArgsConstructor @Slf4j @Setter
//public class JwtValidatorFilter extends OncePerRequestFilter {
//
//    private static final String BEARER = "Bearer ";
//
//    private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
//
//    private final JWTService jwtService;
//
//    private final UserAccountRepository userAccountRepository;
//
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    private Collection<String> filterExcludePath = Set.of("/api/v1/auth");
//
//    private String reIssuedTokenPath = "/api/reissue/token";
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        if (requestURIContainsExcludeURI(request)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (request.getRequestURI().equals(reIssuedTokenPath)) {
//
//            final String refreshToken = request.getHeader(REFRESH_TOKEN_HEADER);
//            if (refreshToken != null) {
//                String subjectEmail = jwtService.extractClaim(
//                        refreshToken, JWTService.TokenType.REFRESH, Claims::getSubject);
//
//                userAccountRepository.findByEmail(subjectEmail).ifPresent(userAccount -> {
//                    String reIssuedRefreshToken = jwtService.generateRefreshToken(subjectEmail);
//                    RefreshToken newRefreshToken = RefreshToken.create().token(reIssuedRefreshToken ).build();
//                    refreshTokenRepository.updateToken(newRefreshToken.getToken());
//
//
//                    String reIssuedAccessToken = jwtService.generateAccessToken(subjectEmail);
//
//                    response.setHeader(REFRESH_TOKEN_HEADER, reIssuedAccessToken);
//                    response.setHeader(AUTHORIZATION, BEARER + reIssuedAccessToken);
//                });
//            }
//            return;
//        }
//
//        final String authHeader = request.getHeader(AUTHORIZATION);
//        if (authHeader == null || !authHeader.startsWith(BEARER)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        final String accessToken = authHeader.substring(7);
//        if (jwtService.isAccessTokenValid(accessToken)) {
//            log.info("access token valid");
//        }
//        final String userEmail;
//        filterChain.doFilter(request, response);
//    }
//
//    private boolean requestURIContainsExcludeURI(HttpServletRequest request) {
//        return filterExcludePath.stream()
//                .anyMatch(excludePath -> request.getRequestURI().contains(excludePath));
//    }
//
//    private Optional<String> extractRefreshToken(HttpServletRequest request) {
//        return Optional.ofNullable(request.getHeader(REFRESH_TOKEN_HEADER));
//    }
//
//}
//
//// RefreshToken 검사 (RefreshToken 이 요청으로 왔다는 것은 RefreshToken 을 재발급해주어야하는 이유밖에 없음.)
//// 1. RefreshToken 의 Signature 를 검사해야 한다.
//// 2. RefreshToken 이 진짜 만료되었는지 확인해야 한다.
//// 3. RefreshToken 을 재발급할떄 AccessToken 또한 재발급해주어야 한다.