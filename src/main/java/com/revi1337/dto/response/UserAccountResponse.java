package com.revi1337.dto.response;

import com.revi1337.dto.UserAccountDto;

public record UserAccountResponse(
        Long id,
        String email,
        String username,
        String password
) {
    public static UserAccountResponse of(Long id,
                                         String email,
                                         String username,
                                         String password) {
        return new UserAccountResponse(id, email, username, password);
    }

    public static UserAccountResponse from(UserAccountDto userAccountDto) {
        return new UserAccountResponse(
                userAccountDto.id(),
                userAccountDto.email(),
                userAccountDto.username(),
                userAccountDto.password()
        );
    }
}
