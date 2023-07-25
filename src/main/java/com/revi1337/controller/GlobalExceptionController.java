package com.revi1337.controller;

import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.common.ErrorResponse;
import com.revi1337.exception.RootException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
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
        APIResponse<Object> apiResponse = APIResponse.of(errorResponse);
        return ResponseEntity
                .status(errorResponse.statusCode())
                .body(apiResponse);
    }

}
