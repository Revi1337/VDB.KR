package com.revi1337.dto.response.nvd;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CVEResponse(
        @JsonProperty("vulnerabilities") List<Vulnerabilities> vulnerabilities
) {}

record Vulnerabilities(
        Cve cve
) {}

record Cve (
        String id,
        String published,
        String lastModified,
        List<Description> descriptions,
        Metrics metrics,
        List<Weaknesses> weaknesses
) {}

record Description(
        String lang,
        String value
) {}

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
record Metrics (
        List<CvssMetricV30> cvssMetricV30,
        List<CvssMetricV31> cvssMetricV31
) {}

record CvssMetricV30 (
        CvssData cvssData,
        float exploitabilityScore,
        float impactScore
) {}

record CvssMetricV31 (
        CvssData cvssData,
        float exploitabilityScore,
        float impactScore
) {}

record CvssData (
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
        String baseSeverity
) {}

record Weaknesses(
    List<Description> description
) {
    record Description(
            String lang,
            String value
    ){}
}

