package com.revi1337.service;

import com.revi1337.domain.*;
import com.revi1337.dto.response.nvd.NVDResponse;
import com.revi1337.repository.MetrixRepository;
import com.revi1337.repository.UserAccountRepository;
import com.revi1337.repository.VulnerabilityRepository;
import com.revi1337.repository.WeaknessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service @RequiredArgsConstructor
public class APIService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final VulnerabilityRepository vulnerabilityRepository;

    private final MetrixRepository metrixRepository;

    private List<Vulnerability> vulnerabilityArrayList = new ArrayList<>();

    private List<Metrix> metrixArrayList = new ArrayList<>();

    @Transactional
    public void processing(NVDResponse nvdResponse) {
        extractSchemas(nvdResponse);
        metrixRepository.saveAll(metrixArrayList);
        vulnerabilityRepository.saveAll(vulnerabilityArrayList);
    }

    private void extractSchemas(NVDResponse nvdResponse) {
        List<NVDResponse.Vulnerability> vulnerabilities = nvdResponse.vulnerabilities();
        for (NVDResponse.Vulnerability vulnerability : vulnerabilities) {
            NVDResponse.Vulnerability.Cve cve = vulnerability.cve();

            Metrix metrix = extractMetrix(cve);
            Vulnerability vuln = Vulnerability.create()
                    .cveId(cve.id())
                    .published(LocalDateTime.parse(cve.published(), FORMATTER))
                    .description(cve.descriptions().get(0).value())
                    .metrix(metrix)
                    .build();

            List<NVDResponse.Vulnerability.Cve.Weak> weaknesses = cve.weaknesses();
            if (weaknesses != null) {
                for (NVDResponse.Vulnerability.Cve.Weak weakness : weaknesses) {
                    List<NVDResponse.Vulnerability.Cve.Weak.Description> descriptions = weakness.description();
                    for (NVDResponse.Vulnerability.Cve.Weak.Description description : descriptions) {
                        vuln.addWeakness(
                                Weak.create()
                                        .weaknessType(description.value())
                                        .build()
                        );
                    }
                }
            }

            metrixArrayList.add(metrix);
            vulnerabilityArrayList.add(vuln);
        }
    }


    private Metrix extractMetrix(NVDResponse.Vulnerability.Cve cve) {
        Score score = extractScore(cve);
        PrejudiceEffect prejudiceEffect = extractPrejudiceEffect(cve);

        NVDResponse.Vulnerability.Cve.Metrics metrics = cve.metrics();
        Metrix metrix = null;

        if (metrics.cvssMetricV31() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = metrics.cvssMetricV31().get(0).cvssData();
                return Metrix.create()
                    .vectorString(cvssData.vectorString())
                    .attackVector(cvssData.attackVector())
                    .attackComplex(cvssData.attackComplexity())
                    .privilegeRequired(cvssData.privilegesRequired())
                    .userInteraction(cvssData.userInteraction())
                    .scope(cvssData.scope())
                    .score(score)
                    .prejudiceEffect(prejudiceEffect)
                    .build();
        }

        if (metrics.cvssMetricV30() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = metrics.cvssMetricV30().get(0).cvssData();
            return Metrix.create()
                    .vectorString(cvssData.vectorString())
                    .attackVector(cvssData.attackVector())
                    .attackComplex(cvssData.attackComplexity())
                    .privilegeRequired(cvssData.privilegesRequired())
                    .userInteraction(cvssData.userInteraction())
                    .scope(cvssData.scope())
                    .score(score)
                    .prejudiceEffect(prejudiceEffect)
                    .build();
        }

        if (metrics.cvssMetricV2() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2 cvssMetricV2 = metrics.cvssMetricV2().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2.CvssData cvssData = cvssMetricV2.cvssData();
            return Metrix.create()
                    .vectorString(cvssData.vectorString())
                    .attackVector(cvssData.accessVector())
                    .attackComplex(cvssData.accessComplexity())
                    .privilegeRequired(cvssData.authentication())
                    .scope("????")
                    .userInteraction(String.valueOf(cvssMetricV2.userInteractionRequired()))
                    .score(score)
                    .prejudiceEffect(prejudiceEffect)
                    .build();
        }
        return Metrix.create()
                .vectorString("")
                .attackVector("")
                .attackComplex("")
                .privilegeRequired("")
                .scope("")
                .userInteraction("")
                .score(
                        Score.create()
                                .baseScore(0F)
                                .baseSeverity("")
                                .exploitabilityScore(0F)
                                .impactScore(0F)
                                .build()
                )
                .prejudiceEffect(
                        PrejudiceEffect.create()
                                .confidentialityEffect("")
                                .integrityEffect("")
                                .availabilityEffect("")
                                .build()
                )
                .build();
    }

    private Score extractScore(NVDResponse.Vulnerability.Cve cve) {
        NVDResponse.Vulnerability.Cve.Metrics metrics = cve.metrics();
        Score score = null;

        if (metrics.cvssMetricV31() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3 cvssMetricV3 = metrics.cvssMetricV31().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = cvssMetricV3.cvssData();
            return Score.create()
                    .baseScore(cvssData.baseScore())
                    .baseSeverity(cvssData.baseSeverity())
                    .exploitabilityScore(cvssMetricV3.exploitabilityScore())
                    .impactScore(cvssMetricV3.impactScore())
                    .build();
        }

        if (metrics.cvssMetricV30() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3 cvssMetricV3 = metrics.cvssMetricV30().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = cvssMetricV3.cvssData();
            return Score.create()
                    .baseScore(cvssData.baseScore())
                    .baseSeverity(cvssData.baseSeverity())
                    .exploitabilityScore(cvssMetricV3.exploitabilityScore())
                    .impactScore(cvssMetricV3.impactScore())
                    .build();
        }

        if (metrics.cvssMetricV2() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2 cvssMetricV2 = metrics.cvssMetricV2().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2.CvssData cvssData = cvssMetricV2.cvssData();
            return Score.create()
                    .baseScore(cvssData.baseScore())
                    .baseSeverity(cvssMetricV2.baseSeverity())
                    .exploitabilityScore(cvssMetricV2.exploitabilityScore())
                    .impactScore(cvssMetricV2.impactScore())
                    .build();
        }

        return null;
    }

    private PrejudiceEffect extractPrejudiceEffect(NVDResponse.Vulnerability.Cve cve) {
        NVDResponse.Vulnerability.Cve.Metrics metrics = cve.metrics();
        PrejudiceEffect prejudiceEffect = null;

        if (metrics.cvssMetricV31() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3 cvssMetricV3 = metrics.cvssMetricV31().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = cvssMetricV3.cvssData();
            return PrejudiceEffect.create()
                    .confidentialityEffect(cvssData.confidentialityImpact())
                    .integrityEffect(cvssData.integrityImpact())
                    .availabilityEffect(cvssData.availabilityImpact())
                    .build();
        }

        if (metrics.cvssMetricV30() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3 cvssMetricV3 = metrics.cvssMetricV30().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV3.CvssData cvssData = cvssMetricV3.cvssData();
            return PrejudiceEffect.create()
                    .confidentialityEffect(cvssData.confidentialityImpact())
                    .integrityEffect(cvssData.integrityImpact())
                    .availabilityEffect(cvssData.availabilityImpact())
                    .build();
        }

        if (metrics.cvssMetricV2() != null) {
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2 cvssMetricV2 = metrics.cvssMetricV2().get(0);
            NVDResponse.Vulnerability.Cve.Metrics.CvssMetricV2.CvssData cvssData = cvssMetricV2.cvssData();
            return PrejudiceEffect.create()
                    .confidentialityEffect(cvssData.confidentialityImpact())
                    .integrityEffect(cvssData.integrityImpact())
                    .availabilityEffect(cvssData.availabilityImpact())
                    .build();
        }

        return null;
    }

}
