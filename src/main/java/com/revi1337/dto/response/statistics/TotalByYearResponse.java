package com.revi1337.dto.response.statistics;

public record TotalByYearResponse(
        Integer year,
        Long totalCount
) {
}
