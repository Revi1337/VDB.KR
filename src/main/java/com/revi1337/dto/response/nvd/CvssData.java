package com.revi1337.dto.response.nvd;

public record CvssData(
        String version,
        String vectorString,
        String attackVector,
        String attackComplexity,
        String privilegesRequired,
        String userInteraction,
        String scope,
        String confidentialityImpact,
        String integrityImpact,
        String availabilityImpact,
        float baseScore,
        String baseSeverity,
        String accessVector,

        String accessComplexity,
        String authentication
) {
}
