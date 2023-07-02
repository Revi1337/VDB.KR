package com.revi1337.dto.response.nvd;

public record CvssMetricV2(
        CvssData cvssData,
        String baseSeverity,
        float exploitabilityScore,
        float impactScore,

        String source,
        String type,
        boolean acInsufInfo,
        boolean obtainAllPrivilege,
        boolean obtainUserPrivilege,
        boolean obtainOtherPrivilege,
        boolean userInteractionRequired
) {
}
