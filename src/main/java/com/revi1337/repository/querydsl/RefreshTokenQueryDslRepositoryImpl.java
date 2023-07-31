package com.revi1337.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.revi1337.domain.RefreshToken;
import lombok.RequiredArgsConstructor;

import static com.revi1337.domain.QRefreshToken.*;
import static com.revi1337.domain.QUserAccount.*;

@RequiredArgsConstructor
public class RefreshTokenQueryDslRepositoryImpl implements RefreshTokenQueryDslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public RefreshToken findRelatedRefreshToken2(String email) {
        return jpaQueryFactory
                .selectFrom(refreshToken)
                .where(userAccount.email.eq(email))
                .fetchOne();
    }
}
