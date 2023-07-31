package com.revi1337.domain.enumerate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private final String name;
}
