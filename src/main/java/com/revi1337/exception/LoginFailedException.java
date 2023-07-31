package com.revi1337.exception;

public class LoginFailedException extends RootException {
    @Override
    public String getExceptionMessage() {
        return "login failed";
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
