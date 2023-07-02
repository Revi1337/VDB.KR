package com.revi1337.dto.response.nvd;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public record Metrics(
        List<CvssMetricV30> cvssMetricV30,
        List<CvssMetricV31> cvssMetricV31,
        List<CvssMetricV2> cvssMetricV2
) {
}
