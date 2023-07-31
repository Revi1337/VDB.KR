package com.revi1337.dto;


public record RefreshTokenDto(
        Long id,
        String token,
        boolean loggedIn,
        String remoteIpAddress
) {
    public static RefreshTokenDto of(Long id,
                                     String token,
                                     boolean loggedIn,
                                     String remoteIpAddress) {
        return new RefreshTokenDto(id, token, loggedIn, remoteIpAddress);
    }

}
