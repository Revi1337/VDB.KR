package com.revi1337.exception;

public class EmailTokenNotFoundException extends RootException {

    @Override
    public String getExceptionMessage() {
        return "confirm token not found";
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
