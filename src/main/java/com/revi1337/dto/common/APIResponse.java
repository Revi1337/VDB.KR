package com.revi1337.dto.common;

public record APIResponse<T> (T body) {

    public static <T> APIResponse <T> of (T body) {
        return new APIResponse<>(body);
    }

}

