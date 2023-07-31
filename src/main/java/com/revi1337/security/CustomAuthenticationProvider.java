package com.revi1337.security;


import com.revi1337.dto.security.AuthenticatedUserAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j @RequiredArgsConstructor
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticatedUserAccount authenticatedUserAccount =
                (AuthenticatedUserAccount) userDetailsService.loadUserByUsername(authentication.getName());
        String encodedPassword = authenticatedUserAccount.password();
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), encodedPassword))
            throw new BadCredentialsException("password incorrect");
        return UsernamePasswordAuthenticationToken.authenticated(
                authenticatedUserAccount, null, authenticatedUserAccount.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
