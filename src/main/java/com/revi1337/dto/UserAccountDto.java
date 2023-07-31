package com.revi1337.dto;

import com.revi1337.domain.RefreshToken;
import com.revi1337.domain.UserAccount;
import com.revi1337.domain.enumerate.Role;

public record UserAccountDto(
        Long id,
        String email,
        String username,
        String password,
        RefreshToken refreshToken,
        boolean activate,
        Role role
) {
    public static UserAccountDto of(Long id,
                                    String email,
                                    String username,
                                    String password) {
        return UserAccountDto.of(id, email, username, password, null);
    }

    public static UserAccountDto of(Long id,
                                    String email,
                                    String username,
                                    String password,
                                    RefreshToken refreshToken) {
        return UserAccountDto.of(id, email, username, password, refreshToken, false, Role.GUEST);
    }

    public static UserAccountDto of(Long id,
                                    String email,
                                    String username,
                                    String password,
                                    RefreshToken refreshToken,
                                    boolean activate,
                                    Role role) {
        return new UserAccountDto(id, email, username, password, refreshToken, activate, role);
    }

    public UserAccount toEntity(RefreshToken refreshToken) {
        return UserAccount.create()
                .id(id)
                .email(email)
                .username(username)
                .password(password)
                .refreshToken(refreshToken)
                .activate(activate)
                .role(role)
                .build();
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return UserAccountDto.of(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getUsername(),
                userAccount.getPassword(),
                userAccount.getRefreshToken(),
                userAccount.isActivate(),
                userAccount.getRole()
        );
    }

}
