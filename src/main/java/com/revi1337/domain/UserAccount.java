package com.revi1337.domain;

import com.revi1337.domain.common.TimeBasedEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "USER_ACCOUNT")
public class UserAccount extends TimeBasedEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Setter(value = AccessLevel.PROTECTED)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    // *******************Builder Method**********************

    @Builder(builderMethodName = "create")
    private UserAccount(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // *******************BUSINESS LOGIC**********************

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

}
