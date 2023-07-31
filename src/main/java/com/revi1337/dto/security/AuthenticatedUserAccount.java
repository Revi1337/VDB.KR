package com.revi1337.dto.security;

import com.revi1337.domain.enumerate.Role;
import com.revi1337.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record AuthenticatedUserAccount(
        Long id,
        String email,
        String username,
        String password,
        boolean activate,
        Role role
) implements UserDetails {

    public static AuthenticatedUserAccount of(Long id,
                                              String email,
                                              String username,
                                              String password,
                                              boolean activate,
                                              Role role) {
        return new AuthenticatedUserAccount(
                id,
                email,
                username,
                password,
                activate,
                role
        );
    }

    public static AuthenticatedUserAccount from(UserAccountDto userAccountDto) {
        return AuthenticatedUserAccount.of(
                userAccountDto.id(),
                userAccountDto.email(),
                userAccountDto.username(),
                userAccountDto.password(),
                userAccountDto.activate(),
                userAccountDto.role()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(role)
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activate;
    }

}


//package com.revi1337.dto.security;
//
//import com.revi1337.domain.enumerate.Role;
//import com.revi1337.dto.UserAccountDto;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//import java.util.Collection;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public record AuthenticatedUserAccount(
//        Long id,
//        String email,
//        String username,
//        String password,
//        Collection<? extends GrantedAuthority> authorities,
//        boolean activate
//) implements UserDetails {
//
//    public static AuthenticatedUserAccount of(Long id,
//                                              String email,
//                                              String username,
//                                              String password) {
//        return AuthenticatedUserAccount.of(id, email, username, password, false);
//    }
//
//    public static AuthenticatedUserAccount of(Long id,
//                                              String email,
//                                              String username,
//                                              String password,
//                                              boolean activate) {
//        Set<Role> roles = Set.of(Role.USER);
//        return new AuthenticatedUserAccount(
//                id,
//                email,
//                username,
//                password,
//                roles.stream()
//                        .map(Role::getName)
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toUnmodifiableSet()),
//                activate
//        );
//    }
//
//    public static AuthenticatedUserAccount from(UserAccountDto userAccountDto) {
//        return AuthenticatedUserAccount.of(
//                userAccountDto.id(),
//                userAccountDto.email(),
//                userAccountDto.username(),
//                userAccountDto.password(),
//                userAccountDto.activate()
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public String getPassword() {
//        return password;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return activate;
//    }
//
//}
