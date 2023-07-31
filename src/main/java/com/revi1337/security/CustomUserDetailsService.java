package com.revi1337.security;

import com.revi1337.dto.UserAccountDto;
import com.revi1337.dto.security.AuthenticatedUserAccount;
import com.revi1337.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository
                .findByEmail(email)
                .map(UserAccountDto::from)
                .map(AuthenticatedUserAccount::from)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
