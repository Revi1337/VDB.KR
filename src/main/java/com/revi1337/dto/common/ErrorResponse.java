package com.revi1337.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public record ErrorResponse (
        int statusCode,
        String message,
        Map<String, String> validation,
        LocalDateTime timestamp
) {

    public static ErrorResponse of(int statusCode, LocalDateTime timestamp, Map<String, String> validation, String message) {
        return new ErrorResponse(statusCode, message, validation, timestamp);
    }

    public static ErrorResponse of(int statusCode, String message, Map<String, String> validation) {
        return ErrorResponse.of(statusCode, LocalDateTime.now(), validation, message);
    }

    public static ErrorResponse of(int statusCode, String message) {
        return ErrorResponse.of(statusCode, message, new HashMap<>());
    }

}


//import java.util.HashMap;
//import java.util.Map;
//
//public record ErrorResponse (
//        int statusCode,
//        String message,
//        Map<String, String> validation
//) {
//
//    public static ErrorResponse of(int statusCode, String message, Map<String, String> validation) {
//        return new ErrorResponse(statusCode, message, validation);
//    }
//
//    public static ErrorResponse of(int statusCode, String message) {
//        return ErrorResponse.of(statusCode, message, new HashMap<>());
//    }
//
//}

