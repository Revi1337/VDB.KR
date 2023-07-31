package com.revi1337.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Collection;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record APIResponse<T> (
        Header header,
        T payload,
        ErrorResponse error
) {

    public static <T> APIResponse <T> of (ErrorResponse error) {
        return APIResponse.of(null, null, error);
    }

    public static <T> APIResponse<T> of (T payload) {
        return APIResponse.of(
                Header.of(payload),
                payload,
                null
        );
    }

    private static <T> APIResponse <T> of (Header header, T payload, ErrorResponse error) {
        return new APIResponse<>(header, payload, error);
    }

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    record Header(
            Integer statusCode,
            Integer totalResults,
            LocalDateTime timestamp
    ) {
        static Header of(Object object) {
            if (object instanceof Collection<?>) {
                return new Header(200, ((Collection<?>) object).size(), LocalDateTime.now());
            }
            return new Header(200, 1, LocalDateTime.now());
        }
    }

}

//import com.fasterxml.jackson.annotation.JsonInclude;

//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
//public record APIResponse<T> (
//        T payload,
//        ErrorResponse error
//) {
//
//    public static <T> APIResponse <T> of (T payload) {
//        return new APIResponse<>(payload, null);
//    }
//
//    public static <T> APIResponse <T> of (ErrorResponse error) {
//        return new APIResponse<>(null, error);
//    }
//
//    public static <T> APIResponse <T> of (T payload, ErrorResponse error) {
//        return new APIResponse<>(payload, error);
//    }
//
//}


//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
//public record APIResponse<T> (
//        Header header,
//        T payload,
//        ErrorResponse error
//) {
//
//    public static <T> APIResponse<Collection<T>> of (Collection<T> payload) {
//        return APIResponse.of(
//                new Header(200, payload.size()),
//                payload,
//                null
//        );
//    }
//
//    public static <T> APIResponse<T> of (T payload) {
//        return APIResponse.of(
//                new Header(200, 1),
//                payload,
//                null
//        );
//    }
//
//    public static <T> APIResponse <T> of (ErrorResponse error) {
//        return APIResponse.of(null, null, error);
//    }
//
//    private static <T> APIResponse <T> of (Header header, T payload, ErrorResponse error) {
//        return new APIResponse<>(header, payload, error);
//    }
//
//    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
//    record Header(
//            Integer statusCode,
//            Integer totalResults
////            LocalDateTime timestamp
//    ) {
//    }
//}


