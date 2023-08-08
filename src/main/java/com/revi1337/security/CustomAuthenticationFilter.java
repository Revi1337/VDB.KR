package com.revi1337.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.domain.RefreshToken;
import com.revi1337.domain.UserAccount;
import com.revi1337.dto.request.LoginRequest;
import com.revi1337.dto.security.AuthenticatedUserAccount;
import com.revi1337.repository.UserAccountRepository;
import com.revi1337.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;


@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String REFRESH_TOKEN_HEADER = "Refresh-Token";

    private static final String BEARER = "Bearer ";

    private final ObjectMapper objectMapper;

    private final UserAccountRepository userAccountRepository;

    private final JWTService jwtService;

    public CustomAuthenticationFilter(String login_processing_url,
                                      UserAccountRepository userAccountRepository,
                                      JWTService jwtService,
                                      ObjectMapper objectMapper) {
        super(login_processing_url);
        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException {
        LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        UsernamePasswordAuthenticationToken unauthenticatedToken =
                UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());

        return getAuthenticationManager().authenticate(unauthenticatedToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        AuthenticatedUserAccount authenticatedUserAccount = (AuthenticatedUserAccount) authResult.getPrincipal();

        String accessToken = jwtService.generateAccessToken(
                authenticatedUserAccount.username(), Map.of("email", authenticatedUserAccount.email())
        );

        String refreshToken = userAccountRepository.findRefreshTokenByUserAccountEmail(authenticatedUserAccount.email())
                .map(UserAccount::getRefreshToken)
                .map(RefreshToken::getToken)
                .orElse(null);

//        response.setHeader(REFRESH_TOKEN_HEADER, refreshToken);
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }

}
