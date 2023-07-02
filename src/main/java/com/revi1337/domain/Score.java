package com.revi1337.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Builder(builderMethodName = "create")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable @Getter
public class Score {

    @Column(name = "BASE_SCORE")
    private float baseScore;

    @Column(name = "BASE_SEVERITY")
    private String baseSeverity;

    @Column(name = "EXPLOITABILITY_SCORE")
    private float exploitabilityScore;

    @Column(name = "IMPACT_SCORE")
    private float impactScore;

}
