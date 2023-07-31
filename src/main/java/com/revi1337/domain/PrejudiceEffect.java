package com.revi1337.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@ToString
@Builder(builderMethodName = "create")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Embeddable @Getter
public class PrejudiceEffect {

    @Column(name = "CONFIDENTIALITY_EFFECT")
    private String confidentialityEffect;

    @Column(name = "INTEGRITY_EFFECT")
    private String integrityEffect;

    @Column(name = "AVAILABILITY_EFFECT")
    private String availabilityEffect;

//    @Column(name = "CONFIDENTIALITY_EFFECT", columnDefinition = "varchar(20) default 'NONE'")
//    private String confidentialityEffect;
//
//    @Column(name = "INTEGRITY_EFFECT", columnDefinition = "varchar(20) default 'NONE'")
//    private String integrityEffect;
//
//    @Column(name = "AVAILABILITY_EFFECT", columnDefinition = "varchar(20) default 'NONE'")
//    private String availabilityEffect;

}


//package com.revi1337.domain;
//
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Embeddable;
//import lombok.*;
//
//@ToString
//@Builder(builderMethodName = "create")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
//@Embeddable @Getter
//public class PrejudiceEffect {
//
//    @Column(name = "CONFIDENTIALITY_EFFECT", nullable = false, columnDefinition = "varchar(20) default 'NONE'")


//    private String confidentialityEffect;
//
//    @Column(name = "INTEGRITY_EFFECT", nullable = false, columnDefinition = "varchar(20) default 'NONE'")
//    private String integrityEffect;
//
//    @Column(name = "AVAILABILITY_EFFECT", nullable = false, columnDefinition = "varchar(20) default 'NONE'")
//    private String availabilityEffect;
//
//}
