package com.revi1337.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Builder(builderMethodName = "create") @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity @Table(name = "METRIX")
public class Metrix {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "METRIX_ID")
    public Long id;

    @Column(name = "VECTOR_STRING", nullable = false)
    private String vectorString;

    @Column(name = "ATTACK_VECTOR", nullable = false)
    private String attackVector;

    @Column(name = "ATTACK_COMPLEX", nullable = false)
    private String attackComplex;

    @Column(name = "PRIVILEGE_REQUIRED", nullable = false)
    private String privilegeRequired;

    @Column(name = "USER_INTERACTION", nullable = false)
    private String userInteraction;

    @Column(name = "SCOPE", nullable = false)
    private String scope;

    @Embedded
    private PrejudiceEffect prejudiceEffect;

    @Embedded
    private Score score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metrix metrix)) return false;
        return id != null && Objects.equals(id, metrix.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
