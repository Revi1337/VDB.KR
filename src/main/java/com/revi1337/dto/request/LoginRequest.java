package com.revi1337.dto.request;


import com.revi1337.dto.UserAccountDto;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty(message = "email must be specified") String email,
        @NotEmpty(message = "password must be specified") String password
) {
    public UserAccountDto toDto() {
        return UserAccountDto.of(null, email, null, password);
    }
}
