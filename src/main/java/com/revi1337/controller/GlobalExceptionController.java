package com.revi1337.controller;

import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.common.ErrorResponse;
import com.revi1337.exception.CustomJwtException;
import com.revi1337.exception.RootException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionController {

    /**
     * Validation Exception
     * @param bindException
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> BindExceptionHandler(BindException bindException) {
        HashMap<String, String> hashMap = new HashMap<>();
        bindException.getFieldErrors().iterator()
                .forEachRemaining(fieldError -> hashMap
                        .put(fieldError.getField(), fieldError.getDefaultMessage()));
        ErrorResponse errorResponse = ErrorResponse.of(400, "invalid request", hashMap);
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    /**
     * Handling Type Mismatch
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "type mismatch");
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    /**
     * Prevent Invalid Json Format
     * @param exception
     * @return
     */
    @ExceptionHandler(value = HttpMessageConversionException.class)
    public ResponseEntity<?> rootExceptionHandler(HttpMessageConversionException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "json parse err. do not modify json");
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    /**
     * CustomException
     * @param exception
     * @return
     */
    @ExceptionHandler(value = RootException.class)
    public ResponseEntity<?> rootExceptionHandler(RootException exception) {
        ErrorResponse errorResponse = ErrorResponse.of(
                exception.getStatusCode(), exception.getExceptionMessage());
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    /**
     * Handling Security layer AuthenticationException occur by AuthenticationFailureHandler
     * @AuthenticationFailureHandler executed when Authenticate failed. It throws
     * {@link UsernameNotFoundException}, {@link BadCredentialsException}
     * @param exception
     */
    @ExceptionHandler(value = {UsernameNotFoundException.class, BadCredentialsException.class})
    public ResponseEntity<?> loginAuthenticationExceptionHandler(Exception exception) {
        ErrorResponse errorResponse = ErrorResponse.of(401, "id or password incorrect");
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    /**
     * Handling Security layer AuthenticationException occur by AuthenticationEntryPoint
     * @AuthenticationEntryPoint executed when non authenticated user or insufficient privileges user request endpoint  It throws
     * {@link AuthenticationException}
     * @param exception
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<?> authenticationEntryPointExceptionHandler(Exception exception) {
        String exceptionName = exception.getClass().getSimpleName();
        ErrorResponse errorResponse;
        switch (exceptionName) {
            case "DisabledException" ->
                    errorResponse = ErrorResponse.of(403, "account disabled. plz activate email verification");
            case "InsufficientAuthenticationException" ->
                    errorResponse = ErrorResponse.of(403, "this endpoint must be called by authenticated user");
            default -> errorResponse = ErrorResponse.of(403, "authenticated error");
        }
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = CustomJwtException.class)
    public ResponseEntity<?> customJwtException(CustomJwtException exception) {
        String exceptionName = exception.getJwtException().getClass().getSimpleName();
        ErrorResponse errorResponse;
        switch (exceptionName) {
            case "ExpiredJwtException" -> errorResponse = ErrorResponse.of(401, "token expired");
            case "SignatureException" -> errorResponse = ErrorResponse.of(401, "signature valid failed");
            default -> errorResponse = ErrorResponse.of(401, "token error");
        }
        APIResponse<?> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

}
