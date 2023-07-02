package com.revi1337.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record APIResponse<T> (
        Header header,
        T payload,
        ErrorResponse error
) {

    public static <T> APIResponse <T> of (Header header) {
        return new APIResponse<>(header, null, null);
    }

    public static <T> APIResponse <T> of (T payload) {
        return new APIResponse<>(null, payload, null);
    }

    public static <T> APIResponse <T> of (ErrorResponse error) {
        return new APIResponse<>(null, null, error);
    }

    public static <T> APIResponse <T> of (Header header, T payload, ErrorResponse error) {
        return new APIResponse<>(header, payload, error);
    }

    static class Header {}

}

