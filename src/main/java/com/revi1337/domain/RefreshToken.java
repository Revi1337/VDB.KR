package com.revi1337.domain;

import com.revi1337.domain.common.TimeBasedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken extends TimeBasedEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "REFRESH_ID")
    private Long id;

    private String token;

    private boolean loggedIn;

    private String remoteIpAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RefreshToken that)) return false;
        return id != null & Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // *******************Builder Method**********************

    @Builder(builderMethodName = "create")
    private RefreshToken(Long id, String token, boolean loggedIn, String remoteIpAddress) {
        this.id = id;
        this.token = token;
        this.loggedIn = loggedIn;
        this.remoteIpAddress = remoteIpAddress;
    }

    // *******************Business Logic**********************

}
