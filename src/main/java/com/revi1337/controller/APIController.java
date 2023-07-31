package com.revi1337.controller;


import com.revi1337.dto.request.nvd.CVESearchType;
import com.revi1337.dto.response.nvd.NVDResponse;
import com.revi1337.service.APIService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RestController @RequiredArgsConstructor
public class APIController {

    @Value("${application.api.nvd.api-key}")
    private String nvdApiKey;

    private final DecimalFormat decimalFormat = new DecimalFormat("00");

    private final LocalDateTime currentLocalDateTime = LocalDateTime.now();

    private final RestTemplate restTemplate;

    private final APIService apiService;

    private NVDResponse nvdResponse;

    private static int threadSleepTime = 1000;

    @GetMapping("/test-cve")
    public NVDResponse fetchVulnerabilityData2() throws URISyntaxException, IOException, InterruptedException {
        String uriString = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov/rest/json/cves/2.0")
                .query("noRejected")
                .queryParam("pubStartDate", "2022-01-01T00:00:00.000")
                .queryParam("pubEndDate", "2022-02-28T00:00:00.000")
                .toUriString();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uriString)
                .header("apikey", nvdApiKey)
                .build();

        ResponseEntity<NVDResponse> responseEntity = restTemplate.exchange(requestEntity, NVDResponse.class);
        NVDResponse body = responseEntity.getBody();
        apiService.processing(body);
        return body;
    }

    @GetMapping("/cve")
    public void fetchVulnerabilityData3() throws InterruptedException {
        for (int year = 2023; year <= currentLocalDateTime.getYear(); year++) {
            for (int month = 1; month <= 12; month += 2) {
                YearMonth yearMonth = decisionDateTime(year, month);
                String uriString = createURIString(yearMonth);
                sendRequest(uriString);
                apiService.processing(this.nvdResponse);
                int totalResults = this.nvdResponse.totalResults();
                if (totalResults <= 2000)
                    continue;
                for (int startIndex = 1; startIndex <= (totalResults / 2000); startIndex++) {
                    String uriString2 = createURIString(yearMonth, startIndex);
                    sendRequest(uriString2);
                    apiService.processing(this.nvdResponse);
                }
            }
        }
    }

    @GetMapping("/cve-git")
    public void fetchVulnerabilityPOC(CVESearchType cveSearchType) throws IOException {
        String url = String.format(
                "https://github.com/search?q=%s&type=repositories&s=stars&o=desc", cveSearchType.cve_id()
        );
        Document doc = Jsoup.connect(url).get();
        Elements elementsByClass = doc.getElementsByClass("search-title");
        List<String> strings = elementsByClass.stream()
                .map(element -> element.child(0))
                .map(element -> element.attr("href"))
                .map(element -> "https://github.com" + element)
                .toList();
        for (String string : strings) {
            System.out.println(string);
        }
    }

    private void sendRequest(String uriString) throws InterruptedException {
        try {
            RequestEntity<Void> requestEntity = RequestEntity
                    .get(uriString)
                    .header("apikey", nvdApiKey)
                    .build();
            this.nvdResponse = restTemplate.exchange(requestEntity, NVDResponse.class).getBody();
        } catch (Exception exception) {
            Thread.sleep(threadSleepTime);
            threadSleepTime += 1000;
            sendRequest(uriString);
        }
    }

    private String createURIString(YearMonth yearMonth) {
        YearMonth realYearMonth = YearMonth.of(yearMonth.getYear(), yearMonth.getMonthValue() + 1);
        return UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov/rest/json/cves/2.0")
                .query("noRejected")
                .queryParam("pubStartDate", String.format("%s-%s-01T00:00:00.000", yearMonth.getYear(), decimalFormat.format(yearMonth.getMonthValue())))
                .queryParam("pubEndDate", String.format("%s-%s-%sT00:00:00.000", yearMonth.getYear(), decimalFormat.format(yearMonth.getMonthValue() + 1), realYearMonth.lengthOfMonth()))
                .toUriString();
    }

    private String createURIString(YearMonth yearMonth, int startIndex) {
        YearMonth realYearMonth = YearMonth.of(yearMonth.getYear(), yearMonth.getMonthValue() + 1);
        return UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov/rest/json/cves/2.0")
                .query("noRejected")
                .queryParam("pubStartDate", String.format("%s-%s-01T00:00:00.000", yearMonth.getYear(), decimalFormat.format(yearMonth.getMonthValue())))
                .queryParam("pubEndDate", String.format("%s-%s-%sT00:00:00.000", yearMonth.getYear(), decimalFormat.format(yearMonth.getMonthValue() + 1), realYearMonth.lengthOfMonth()))
                .queryParam("startIndex", String.valueOf(startIndex * 2000))
                .toUriString();
    }

    private YearMonth decisionDateTime(int year, int month) {
        return YearMonth.of(year, month);
    }

}