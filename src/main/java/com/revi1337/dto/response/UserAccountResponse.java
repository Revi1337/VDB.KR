package com.revi1337.dto.response;

import com.revi1337.domain.enumerate.Role;
import com.revi1337.dto.UserAccountDto;

public record UserAccountResponse(
        Long id,
        String email,
        String username,
        boolean activate,
        Role role
) {
    public static UserAccountResponse of(Long id,
                                         String email,
                                         String username,
                                         boolean activate,
                                         Role role) {
        return new UserAccountResponse(id, email, username, activate, role);
    }

    public static UserAccountResponse from(UserAccountDto userAccountDto) {
        return new UserAccountResponse(
                userAccountDto.id(),
                userAccountDto.email(),
                userAccountDto.username(),
                userAccountDto.activate(),
                userAccountDto.role()
        );
    }
}
