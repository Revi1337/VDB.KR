package com.revi1337.exception;

import lombok.Getter;

@Getter
public abstract class RootException extends RuntimeException {

    abstract public String getExceptionMessage();

    abstract public int getStatusCode();
}
