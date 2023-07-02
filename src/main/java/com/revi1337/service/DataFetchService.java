package com.revi1337.service;

import com.revi1337.domain.Metrix;
import com.revi1337.domain.PrejudiceEffect;
import com.revi1337.domain.Score;
import com.revi1337.domain.Vulnerability;
import com.revi1337.dto.response.nvd.*;
import com.revi1337.repository.MetrixRepository;
import com.revi1337.repository.VulnerabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional(readOnly = true)
public class DataFetchService {

    private final VulnerabilityRepository vulnerabilityRepository;

    private final MetrixRepository metrixRepository;

    private final List<Metrix> metrixes = new ArrayList<>();

    private final List<Vulnerability> vulnerabilities = new ArrayList<>();

    public PrejudiceEffect createPrejudiceEffect(String confidentialityEffect, String availabilityEffect, String integrityEffect) {
        return PrejudiceEffect.create()
                .confidentialityEffect(confidentialityEffect)
                .availabilityEffect(availabilityEffect)
                .integrityEffect(integrityEffect)
                .build();
    }

    public String retrieveDescription(List<Description> descriptions) {
        return descriptions
                .stream()
                .filter(description -> description.lang().equals("en"))
                .findFirst()
                .get()
                .value();
    }

    public CvssData determineCvssData(List<CvssMetricV31> cvssMetricV31s,
                                      List<CvssMetricV30> cvssMetricV30s,
                                      List<CvssMetricV2> cvssMetricV2) {
        if (cvssMetricV31s != null)
            return cvssMetricV31s.get(0).cvssData();
        if (cvssMetricV30s != null)
            return cvssMetricV30s.get(0).cvssData();
        if (cvssMetricV2 != null)
            return cvssMetricV2.get(0).cvssData();
        return new CvssData("NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                "NONE",
                0F,
                "NONE",
                "NONE",
                "NONE",
                "NONE");
    }

    @Transactional
    public void processingData(CVEResponse cveResponse) {
        Vulnerability vulnerability = null;
        Metrix metrix = null;
        for (Vulnerabilities vulnerable : cveResponse.vulnerabilities()) {
            Cve cve = vulnerable.cve();
            Metrics metrics = cve.metrics();
//            List<Weaknesses> weaknesses = cve.weaknesses();
            List<CvssMetricV31> cvssMetricV31s = metrics.cvssMetricV31();
            List<CvssMetricV30> cvssMetricV30s = metrics.cvssMetricV30();
            List<CvssMetricV2> cvssMetricV2s = metrics.cvssMetricV2();
            CvssData cvssData = determineCvssData(cvssMetricV31s, cvssMetricV30s, cvssMetricV2s);

            Score score = scoreFactory(cvssMetricV31s, cvssMetricV30s, cvssMetricV2s, cvssData);
            PrejudiceEffect prejudiceEffect = prejudiceEffectFactory(cvssData);

            metrix = metrixFactory(cvssData, prejudiceEffect, score);
            vulnerability = vulnerabilityFactory(cve, metrix);

            metrixes.add(metrix);
            vulnerabilities.add(vulnerability);
        }
        metrixRepository.saveAll(metrixes);
        vulnerabilityRepository.saveAll(vulnerabilities);
    }

    public PrejudiceEffect prejudiceEffectFactory(CvssData cvssData) {
        return PrejudiceEffect.create()
                .confidentialityEffect(cvssData.confidentialityImpact())
                .availabilityEffect(cvssData.availabilityImpact())
                .integrityEffect(cvssData.integrityImpact())
                .build();
    }

    private Vulnerability vulnerabilityFactory(Cve cve, Metrix metrix) {
        String cveId = cve.id();
        String published = cve.published();
        String enDescription = retrieveDescription(cve.descriptions());
        return Vulnerability.create()
                .metrix(metrix)
                .published(published)
                .cveId(cveId)
                .description(enDescription)
                .build();
    }

    private Metrix metrixFactory(CvssData cvssData, PrejudiceEffect prejudiceEffect, Score score) {
        String vectorString = cvssData.vectorString();
        String attackVector = cvssData.attackVector() == null ? cvssData.accessVector() : cvssData.attackVector();
        String attackComplex = cvssData.attackComplexity() == null ? "NONE" : cvssData.attackComplexity();
        String privilegesRequired = cvssData.privilegesRequired() == null ? "NONE" : cvssData.privilegesRequired();
        String userInteraction = cvssData.userInteraction() == null ? "NONE" : cvssData.userInteraction();
        String scope = cvssData.scope() == null ? "NONE" : cvssData.scope();
        return Metrix.create()
                .prejudiceEffect(prejudiceEffect)
                .score(score)
                .scope(scope)
                .userInteraction(userInteraction)
                .privilegeRequired(privilegesRequired)
                .attackVector(attackVector)
                .attackComplex(attackComplex)
                .vectorString(vectorString)
                .build();
    }

    private Score scoreFactory(List<CvssMetricV31> cvssMetricV31s,
                               List<CvssMetricV30> cvssMetricV30s,
                               List<CvssMetricV2> cvssMetricV2s,
                               CvssData cvssData) {
        float baseScore = cvssData.baseScore();
        String baseSeverity = null;
        float exploitabilityScore = 0;
        float impactScore = 0;

        if (cvssMetricV31s != null) {
            exploitabilityScore = cvssMetricV31s.get(0).exploitabilityScore();
            impactScore = cvssMetricV31s.get(0).impactScore();
            baseSeverity = cvssData.baseSeverity();
        }
        if (cvssMetricV30s != null) {
            exploitabilityScore = cvssMetricV30s.get(0).exploitabilityScore();
            impactScore = cvssMetricV30s.get(0).impactScore();
            baseSeverity = cvssData.baseSeverity();
        }
        if (cvssMetricV2s != null) {
            exploitabilityScore = cvssMetricV2s.get(0).exploitabilityScore();
            impactScore = cvssMetricV2s.get(0).impactScore();
            baseSeverity = cvssMetricV2s.get(0).baseSeverity();
        }
        return Score.create()
                .exploitabilityScore(exploitabilityScore)
                .baseScore(baseScore)
                .baseSeverity(baseSeverity)
                .impactScore(impactScore)
                .build();
    }

}
