package com.revi1337.repository.querydsl;

import com.revi1337.domain.RefreshToken;

public interface RefreshTokenQueryDslRepository {

    RefreshToken findRelatedRefreshToken2(String email);
}
