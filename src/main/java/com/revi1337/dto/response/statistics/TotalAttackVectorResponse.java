package com.revi1337.dto.response.statistics;

public record TotalAttackVectorResponse(
        String attackVector,
        Long totalCount
) {
}
