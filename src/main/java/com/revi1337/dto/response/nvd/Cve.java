package com.revi1337.dto.response.nvd;

import java.util.List;

public record Cve <T> (
        String id,
        String published,
        String lastModified,
        List<Description> descriptions,
        Metrics metrics,

        T weaknesses,
        T configurations,
        T references,
        String sourceIdentifier,
        String vulnStatus,
        String cisaExploitAdd,
        String cisaActionDue,
        String cisaRequiredAction,
        String cisaVulnerabilityName,
        List<T> vendorComments,
        T evaluatorComment,
        T evaluatorSolution,
        T evaluatorImpact
) {
}
