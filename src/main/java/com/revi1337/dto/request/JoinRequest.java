package com.revi1337.dto.request;

import com.revi1337.dto.UserAccountDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record JoinRequest(
        @Email(message = "Invalid email format") String email,
        @NotEmpty(message = "username must be specified") String username,
        @NotEmpty(message = "password must be specified") String password
) {
    public UserAccountDto toDto() {
        return UserAccountDto.of(null, email, username, password);
    }
}
