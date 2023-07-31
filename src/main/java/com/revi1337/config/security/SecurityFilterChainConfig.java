package com.revi1337.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.domain.enumerate.Role;
import com.revi1337.repository.RefreshTokenRepository;
import com.revi1337.repository.UserAccountRepository;
import com.revi1337.security.*;
import com.revi1337.service.JWTService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Configuration @EnableWebSecurity @EnableMethodSecurity(securedEnabled = true)
public class SecurityFilterChainConfig {

    private final UserAccountRepository userAccountRepository;

    private final JWTService jwtService;

    private final HandlerExceptionResolver resolver;

    private final ObjectMapper objectMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    public SecurityFilterChainConfig(UserAccountRepository userAccountRepository,
                                     JWTService jwtService,
                                     @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
                                     ObjectMapper objectMapper,
                                     RefreshTokenRepository refreshTokenRepository) {
        this.userAccountRepository = userAccountRepository;
        this.jwtService = jwtService;
        this.resolver = resolver;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll())
                .addFilterAfter(customAuthenticationFilter(), LogoutFilter.class)
                .addFilterAfter(exceptionTranslationFilter(), CustomAuthenticationFilter.class)
                .addFilterAfter(jwtValidatorFilter(), CustomExceptionTranslationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() {
        CustomAuthenticationFilter customLoginFilter =
                new CustomAuthenticationFilter(
                        "/api/v1/auth/login",
                        userAccountRepository,
                        jwtService,
                        objectMapper
                );
        customLoginFilter.setAuthenticationManager(authenticationManager());
        customLoginFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return customLoginFilter;
    }

    @Bean
    public CustomExceptionTranslationFilter exceptionTranslationFilter() {
        return new CustomExceptionTranslationFilter(authenticationEntryPoint());
    }

    @Bean
    public JwtValidatorFilter jwtValidatorFilter() {
        JwtValidatorFilter jwtValidatorFilter = new JwtValidatorFilter(
                objectMapper,
                jwtService,
                userAccountRepository,
                refreshTokenRepository);
        jwtValidatorFilter.setFilterExcludePath(Set.of("/api/v1/auth"));
        jwtValidatorFilter.setReIssuedTokenPath("/api/reissue/token");
        return jwtValidatorFilter;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(userAccountRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(resolver);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(resolver);
    }

    @Bean
    public static RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(
                Stream.of(Role.values())
                        .map(Role::getName)
                        .collect(Collectors.joining(" > "))
        );
        return roleHierarchy;
    }

}

//    @Bean
//    public AuthenticationManager authenticationManager() {
//        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider(
//                userDetailsService(), passwordEncoder()
//        );
//        return new ProviderManager(authenticationProvider);
//    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return email -> userAccountRepository
//                .findByEmail(email)
//                .map(UserAccountDto::from)
//                .map(AuthenticatedUserAccount::from)
//                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
//    }