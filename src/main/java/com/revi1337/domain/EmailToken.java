package com.revi1337.domain;

import com.revi1337.domain.common.TimeBasedEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class EmailToken extends TimeBasedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "EMAIL_TOKEN")
    private Long id;

    @Column(name = "TOKEN", nullable = false)
    private String token;

    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDateTime expiredAt;

    @Setter(AccessLevel.PRIVATE)
    @Column(name = "CONFIRMED_AT")
    private LocalDateTime confirmedAt;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "USER_ACCOUNT_ID")
    private UserAccount userAccount;

    // *******************Builder Method**********************

    @Builder(builderMethodName = "create")
    private EmailToken(Long id,
                       String token,
                       LocalDateTime expiredAt,
                       LocalDateTime confirmedAt,
                       UserAccount userAccount) {
        this.id = id;
        this.token = token;
        this.expiredAt = expiredAt;
        this.confirmedAt = confirmedAt;
        this.userAccount = userAccount;
    }

    // *******************BUSINESS LOGIC**********************

    public void confirmEmailToken(String token, LocalDateTime confirmedAt) {
        this.token = token;
        this.confirmedAt = confirmedAt;
    }

}
