package com.revi1337.dto.response.nvd;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record NVDResponse (
        int totalResults,
        List<Vulnerability> vulnerabilities
) {

    public record Vulnerability(
        Cve cve
    ) {

        public record Cve(
                String id,
                String published,
                String lastModified,
                List<Description> descriptions,
                Metrics metrics,
                List<Weak> weaknesses
        ) {

            public record Description (
                    String lang,
                    String value
            ){}

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            public record Metrics (
                    List<CvssMetricV2> cvssMetricV2,
                    List<CvssMetricV3> cvssMetricV30,
                    List<CvssMetricV3> cvssMetricV31
            ){

                public record CvssMetricV2 (
                        CvssData cvssData,
                        String baseSeverity,
                        int exploitabilityScore,
                        int impactScore,
                        boolean userInteractionRequired
                ){
                    public record CvssData(
                            String vectorString,
                            String accessVector,
                            String accessComplexity,
                            String authentication,
                            String confidentialityImpact,
                            String integrityImpact,
                            String availabilityImpact,
                            int baseScore
                    ){}
                }

                public record CvssMetricV3 (
                    CvssData cvssData,
                    int exploitabilityScore,
                    int impactScore
                ){
                    public record CvssData(
                            String vectorString,
                            String attackVector,
                            String attackComplexity,
                            String privilegesRequired,
                            String userInteraction,
                            String scope,
                            String confidentialityImpact,
                            String integrityImpact,
                            String availabilityImpact,
                            int baseScore,
                            String baseSeverity
                    ){}
                }

//                public record CvssMetricV31 (
//                        CvssData cvssData,
//                        int exploitabilityScore,
//                        int impactScore
//                ){
//                    public record CvssData(
//                            String vectorString,
//                            String attackVector,
//                            String attackComplexity,
//                            String privilegesRequired,
//                            String userInteraction,
//                            String confidentialityImpact,
//                            String availabilityImpact,
//                            int baseScore,
//                            String baseSeverity
//                    ){}
//                }
            }

            public record Weak (
                    List<Description> description
            ){

                public record Description (
                        String value
                ){}
            }
        }
    }
}
