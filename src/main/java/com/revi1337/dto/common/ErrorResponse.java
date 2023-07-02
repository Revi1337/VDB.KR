package com.revi1337.dto.common;

import java.util.HashMap;
import java.util.Map;

public record ErrorResponse (
        int statusCode,
        String message,
        Map<String, String> validation
) {

    public static  ErrorResponse of(int statusCode, String message, Map<String, String> validation) {
        return new ErrorResponse(statusCode, message, validation);
    }

    public static  ErrorResponse of(int statusCode, String message) {
        return new ErrorResponse(statusCode, message, new HashMap<>());
    }

}

