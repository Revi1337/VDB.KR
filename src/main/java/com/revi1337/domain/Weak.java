package com.revi1337.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter @ToString
@Builder(builderMethodName = "create")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Table(name = "WEAKNESS")
public class Weak {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "WEAKNESS_ID")
    private Long id;

    @ToString.Exclude
    @Setter(AccessLevel.PACKAGE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VULNERABILITY_ID")
    private Vulnerability vulnerability;

    @Column(name = "WEAKNESS_TYPE")
    private String weaknessType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weak weakness)) return false;
        return id != null && Objects.equals(id, weakness.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
