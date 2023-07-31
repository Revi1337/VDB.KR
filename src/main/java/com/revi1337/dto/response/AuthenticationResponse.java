package com.revi1337.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
    public static AuthenticationResponse of(String accessToken, String refreshToken) {
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    public static AuthenticationResponse of(String accessToken) {
        return new AuthenticationResponse(accessToken, null);
    }
}
