package com.revi1337.exception;

public class TokenExpiredException extends RootException {
    @Override
    public String getExceptionMessage() {
        return "token expired";
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
