package com.revi1337.dto;

import com.revi1337.domain.UserAccount;

public record UserAccountDto(
        Long id,
        String email,
        String username,
        String password
) {
    public static UserAccountDto of(Long id,
                                    String email,
                                    String username,
                                    String password) {
        return new UserAccountDto(id, email, username, password);
    }

    public UserAccount toEntity() {
        return UserAccount.create()
                .id(id)
                .email(email)
                .username(username)
                .password(password)
                .build();
    }

    public static UserAccountDto from(UserAccount userAccount) {
        return UserAccountDto.of(
                userAccount.getId(),
                userAccount.getEmail(),
                userAccount.getUsername(),
                userAccount.getPassword()
        );
    }

}
