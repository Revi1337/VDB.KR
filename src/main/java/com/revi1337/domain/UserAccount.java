package com.revi1337.domain;

import com.revi1337.domain.common.TimeBasedEntity;
import com.revi1337.domain.enumerate.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "USER_ACCOUNT")
public class UserAccount extends TimeBasedEntity {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "USER_ACCOUNT_ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Setter(value = AccessLevel.PRIVATE)
    @Column(name = "ACTIVATE", nullable = false)
    private boolean activate;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "REFRESH_ID")
    private RefreshToken refreshToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // *******************Builder Method**********************

    @Builder(builderMethodName = "create")
    private UserAccount(Long id,
                        String username,
                        String password,
                        String email,
                        RefreshToken refreshToken,
                        boolean activate,
                        Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.refreshToken = refreshToken;
        this.activate = activate;
        this.role = role;
    }

    // *******************BUSINESS LOGIC**********************

    public void changePassword(String newPassword) { this.password = newPassword; }

    public void activateUserAccount(boolean activate, Role role) {
        this.activate = activate;
        this.role = role;
    }

}
