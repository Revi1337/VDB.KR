package com.revi1337.exception;

public class DuplicateEmailException extends RootException {

    @Override
    public String getExceptionMessage() {
        return "duplicate email. plz specify other email";
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
