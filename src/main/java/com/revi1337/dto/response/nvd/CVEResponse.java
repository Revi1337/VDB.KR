package com.revi1337.dto.response.nvd;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CVEResponse(
        @JsonProperty("vulnerabilities") List<Vulnerabilities> vulnerabilities,

        int resultsPerPage,
        int startIndex,
        int totalResults,
        String format,
        String version,
        String timestamp
) {}

//public record CVERespon(
////        T resultsPerPage,
////        T startIndex,
////        T format,
////        T version,
////        T timestamp,
////        List<Vulnerabilities> vulnerabilities
////) {}
////se <T>