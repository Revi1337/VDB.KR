package com.revi1337.exception;

import io.jsonwebtoken.JwtException;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomJwtException extends AuthenticationException {

    private final JwtException jwtException;

    public CustomJwtException(JwtException jwtException) {
        super(jwtException.getLocalizedMessage());
        this.jwtException = jwtException;
    }
}
