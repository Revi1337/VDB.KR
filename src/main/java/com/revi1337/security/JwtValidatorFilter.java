package com.revi1337.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.domain.RefreshToken;
import com.revi1337.dto.UserAccountDto;
import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.common.ErrorResponse;
import com.revi1337.dto.security.AuthenticatedUserAccount;
import com.revi1337.repository.RefreshTokenRepository;
import com.revi1337.repository.UserAccountRepository;
import com.revi1337.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
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

    private final ObjectMapper objectMapper;

    private final JWTService jwtService;

    private final UserAccountRepository userAccountRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private Collection<String> filterExcludePath = Set.of("/api/v1/auth");

    private String reIssuedTokenPath = "/api/reissue/token";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // check exclude path
        if (requestURIContainsExcludeURI(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // reissue token logic
        if (request.getRequestURI().equals(reIssuedTokenPath)) {
            Cookie[] extractedCookies = request.getCookies();
            if (extractedCookies == null) {
                sendError(response, HttpStatus.BAD_REQUEST, "refresh token cookie needed");
                return;
            }
            String refreshToken = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_HEADER))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);

            if (refreshToken == null) {
                sendError(response, HttpStatus.BAD_REQUEST, "refresh token needed");
                return;
            }

            String subjectEmail = jwtService.extractClaim(
                    refreshToken, JWTService.TokenType.REFRESH, Claims::getSubject);
            if (refreshTokenRepository.findByToken(refreshToken).isEmpty()) {
                sendError(response, HttpStatus.BAD_REQUEST, "refresh token not found");
                return;
            }

            userAccountRepository.findByEmail(subjectEmail).ifPresent(
                    userAccount -> {
                        String reIssuedRefreshToken = jwtService.generateRefreshToken(subjectEmail);
                        RefreshToken newRefreshToken = RefreshToken.create().token(reIssuedRefreshToken).build();
                        refreshTokenRepository.updateToken(newRefreshToken.getToken());
                        ResponseCookie responseCookie = ResponseCookie.from(REFRESH_TOKEN_HEADER, newRefreshToken.getToken())
                                .path("/")
                                .httpOnly(true)
                                .sameSite("None")
                                .secure(true)
                                .build();

                        String reIssuedAccessToken = jwtService.generateAccessToken(subjectEmail);

                        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
                        response.setHeader(AUTHORIZATION, BEARER + reIssuedAccessToken);
                    });
            return;
        }

        // check access-token
        final String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String accessToken = authHeader.substring(7);
        if (jwtService.isAccessTokenValid(accessToken)) {
            log.info("token valid");
            String userEmail = jwtService.extractAllClaims(
                    accessToken, JWTService.TokenType.ACCESS).get("email", String.class);
            userAccountRepository.findByEmail(userEmail)
                    .map(UserAccountDto::from)
                    .map(AuthenticatedUserAccount::from)
                    .ifPresent(
                            principal -> {
                                UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken
                                .authenticated(principal, null, principal.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                    );
        }
        filterChain.doFilter(request, response);
    }

    private boolean requestURIContainsExcludeURI(HttpServletRequest request) {
        return filterExcludePath.stream()
                .anyMatch(excludePath -> request.getRequestURI().contains(excludePath));
    }

    private void sendError(HttpServletResponse httpServletResponse, HttpStatus status, String error) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(status.value(), error);
        String jsonString = objectMapper.writeValueAsString(APIResponse.of(errorResponse));
        httpServletResponse.getWriter().println(jsonString);
        httpServletResponse.setStatus(status.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public Optional<Cookie[]> extractCookies(HttpServletRequest httpServletRequest) {
        return Optional.of(httpServletRequest.getCookies());
    }

}
