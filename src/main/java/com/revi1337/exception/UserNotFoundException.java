package com.revi1337.exception;

public class UserNotFoundException extends RootException {

    private static final String MESSAGE = "user not found";

    @Override
    public String getExceptionMessage() {
        return MESSAGE;
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
