package com.revi1337.dto.common;

public record ErrorResponse <T> (
        int statusCode,
        String reason,
        T validation
) {

    public static <T> ErrorResponse <T> of(int statusCode, String reason, T body) {
        return new ErrorResponse<>(statusCode, reason, body);
    }

}
