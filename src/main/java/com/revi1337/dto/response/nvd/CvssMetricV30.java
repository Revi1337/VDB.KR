package com.revi1337.dto.response.nvd;

public record CvssMetricV30(
        CvssData cvssData,
        float exploitabilityScore,
        float impactScore,

        String source,
        String type
) {


}
