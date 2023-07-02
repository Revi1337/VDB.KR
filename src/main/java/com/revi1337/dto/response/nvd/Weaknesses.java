package com.revi1337.dto.response.nvd;

import java.util.List;

public record Weaknesses(
        List<Description> description
) {
    public record Description(
            String lang,
            String value
    ) {
    }
}
