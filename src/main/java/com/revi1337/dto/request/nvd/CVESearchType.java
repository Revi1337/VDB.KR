package com.revi1337.dto.request.nvd;


public record CVESearchType(
        String cve_id
) {
    public CVESearchType(String cve_id) {
        this.cve_id = cve_id == null ? "CVE-2017-0144" : cve_id.toUpperCase();
    }

}