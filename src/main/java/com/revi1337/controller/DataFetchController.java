package com.revi1337.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.dto.request.nvd.CVESearchType;
import com.revi1337.dto.response.nvd.CVEResponse;
import com.revi1337.service.DataFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


// TODO 나중에 Controller 말고, @Scheduling 에 의해 실행되는 메서드로 빼줄 것임.
@RestController @RequiredArgsConstructor
public class DataFetchController {

    private final DataFetchService dummyService;

    private final List<String> failureStack = new ArrayList<>();

    private final LocalDateTime currentLocalDateTime = LocalDateTime.now();

//    @GetMapping("/prejudice")
//    public PrejudiceEffect prejudice() {
//        return dummyService.createPrejudiceEffect("HIGH", "HIGH", "HIGH");
//    }

    @GetMapping("/cve2")
    public void fetchCveByIdw(CVESearchType cveSearchType) throws URISyntaxException, IOException, InterruptedException {
//        CVEResponse cveResponse = null;
//        for (int year = 2010; year <= 2022; year++) {
//            for (int month = 1; month <= 12; month += 2) {
//                String uriString = createUriString(year, month,0);
//                HttpResponse<String> response = fetchData(uriString);
//                try {
//                    Thread.sleep(1000);
//                    cveResponse = getResponseType(response);
//                } catch (JsonParseException ex) {
//                    failureStack.add(uriString);
//                    continue;
//                }
//                dummyService.processingData(cveResponse);
//                if (cveResponse.totalResults() <= 2000)
//                    continue;
//                for (int i = 1; i <= (cveResponse.totalResults() / 2000); i++) {
//                    Thread.sleep(1000);
//                    String uriString2 = createUriString(year, month,i * 2000);
//                    HttpResponse<String> response2 = fetchData(uriString2);
//                    CVEResponse cveResponse2 = getResponseType(response2);
//                    dummyService.processingData(cveResponse2);
//                }
//            }
//        }
//        System.out.println(failureStack);

//        String uriString = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
//                .path("/rest/json/cves/2.0")
//                .query("sourceIdentifier={sourceIdentifier}")
//                .buildAndExpand("cve@mitre.org")
//                .toUriString();
//        HttpResponse<String> httpResponse = fetchData(uriString);
//        CVEResponse cveResponse = getResponseType(httpResponse);
//        dummyService.processingData(cveResponse);
//        int totalResults = cveResponse.totalResults();
//        for (int startIndex = 1; startIndex <= (totalResults / 2000) ; startIndex++) {
//            String uriString2 = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
//                    .path("/rest/json/cves/2.0")
//                    .query("sourceIdentifier={sourceIdentifier}")
//                    .query("startIndex={startIndex}")
//                    .buildAndExpand("cve@mitre.org", String.valueOf(startIndex * 2000))
//                    .toUriString();
//            HttpResponse<String> httpResponse2 = fetchData(uriString2);
//            CVEResponse cveResponse2 = getResponseType(httpResponse2);
//            dummyService.processingData(cveResponse2);
//        }

//        ////////////////////////////////////////////////////////////////////////
//
//        String day = "";
//        for (int year = 2010; year <= currentLocalDateTime.getYear(); year++) {
////            Thread.sleep(10000);
//            for (int month = 1; month <= 12; month += 2) {
//                CVEResponse cveResponse = null;
//                if (month == 1) day = "28";
//                else if (month == 3 || month == 5) day = "30";
//                else day = "31";
//
//                DecimalFormat decimalFormat = new DecimalFormat("00");
//                String uriString = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
//                        .path("/rest/json/cves/2.0")
//                        .query("pubStartDate={pubStartDate}")
//                        .query("pubEndDate={pubEndDate}")
//                        .buildAndExpand(
//                                String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(month)),
//                                String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(month + 1), day)
//                        )
//                        .toUriString();
//                System.out.println(uriString);
//
//                HttpResponse<String> httpResponse = fetchData(uriString);
//                try {
//                    cveResponse = getResponseType(httpResponse);
//                } catch (JsonParseException | NullPointerException ex) {
//                    failureStack.add(uriString);
//                    continue;
//                }
//                dummyService.processingData(cveResponse);
//                if (cveResponse.totalResults() <= 2000) {
//                    Thread.sleep(2000);
//                    continue;
//                }
//                int totalResults = cveResponse.totalResults();
//
//                for (int startIndex = 1; startIndex <= (totalResults / 2000); startIndex++) {
////                for (int startIndex = 1; startIndex <= 2; startIndex++) {
//                    String uriString2 = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
//                            .path("/rest/json/cves/2.0")
//                            .query("pubStartDate={pubStartDate}")
//                            .query("pubEndDate={pubEndDate}")
//                            .query("startIndex={startIndex}")
//                            .buildAndExpand(
//                                    String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(month)),
//                                    String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(month + 1), day),
//                                    String.valueOf(startIndex * 2000)
//                            )
//                            .toUriString();
//                    System.out.println(uriString2);
//                    CVEResponse cveResponse2;
//                    HttpResponse<String> httpResponse2 = fetchData(uriString2);
//                    try {
//                        cveResponse2 = getResponseType(httpResponse2);
//                    } catch (JsonParseException | NullPointerException ex) {
//                        failureStack.add(uriString2);
//                        continue;
//                    }
//                    dummyService.processingData(cveResponse2);
//                }
//                System.out.println();
//            }
//        }
//        System.out.println(failureStack);

        ////////////////////////////////////////////////////////////////////////

        String day = "";
        for (int year = 2010; year <= currentLocalDateTime.getYear(); year++) {
//            Thread.sleep(10000);
            for (int month = 1; month <= 12; month += 2) {
                CVEResponse cveResponse = null;
                if (month == 1) day = "28";
                else if (month == 3 || month == 5) day = "30";
                else day = "31";

                DecimalFormat decimalFormat = new DecimalFormat("00");
                String uriString = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                        .path("/rest/json/cves/2.0")
                        .query("pubStartDate={pubStartDate}")
                        .query("pubEndDate={pubEndDate}")
                        .query("startIndex={startIndex}")
                        .buildAndExpand(
                                String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(month)),
                                String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(month + 1), day),
                                "0"
                        )
                        .toUriString();
                System.out.println(uriString);

                HttpResponse<String> httpResponse = fetchData(uriString);
                try {
                    cveResponse = getResponseType(httpResponse);
                } catch (JsonParseException | NullPointerException ex) {
                    failureStack.add(uriString);
                    Thread.sleep(3000);
                    continue;
                }
                dummyService.processingData(cveResponse);
                if (cveResponse.totalResults() <= 2000) {
                    Thread.sleep(3000);
                    continue;
                }
                int totalResults = cveResponse.totalResults();

                for (int startIndex = 1; startIndex <= (totalResults / 2000); startIndex++) {
//                for (int startIndex = 1; startIndex <= 2; startIndex++) {
                    String uriString2 = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                            .path("/rest/json/cves/2.0")
                            .query("pubStartDate={pubStartDate}")
                            .query("pubEndDate={pubEndDate}")
                            .query("startIndex={startIndex}")
                            .buildAndExpand(
                                    String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(month)),
                                    String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(month + 1), day),
                                    String.valueOf(startIndex * 2000)
                            )
                            .toUriString();
                    System.out.println(uriString2);
                    CVEResponse cveResponse2;
                    HttpResponse<String> httpResponse2 = fetchData(uriString2);
                    try {
                        cveResponse2 = getResponseType(httpResponse2);
                    } catch (JsonParseException | NullPointerException ex) {
                        failureStack.add(uriString2);
                        Thread.sleep(3000);
                        continue;
                    }
                    dummyService.processingData(cveResponse2);
                }
                System.out.println();
            }
        }
        System.out.println(failureStack);
    }

    private CVEResponse getResponseType(HttpResponse<String> responseType) throws JsonProcessingException {
        CVEResponse cveResponse = null;
            String responseBody = responseType.body();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        cveResponse = objectMapper.readValue(responseBody, CVEResponse.class);
        return cveResponse;
    }

    private HttpResponse<String> fetchData(String uriString) throws URISyntaxException, IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        java.net.http.HttpRequest request = java.net.http.HttpRequest
                .newBuilder()
                .uri(new URI(uriString))
                .setHeader(HttpHeaders.ACCEPT_ENCODING, "identity")
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String createUriString(int year, int startMonth, int startIndex) {
        String day = "";
        if (startMonth == 1) day = "28";
        else if (startMonth == 3 || startMonth == 5) day = "30";
        else day = "31";

        DecimalFormat decimalFormat = new DecimalFormat("00");
        if (startIndex == 0) {
            return UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                    .path("/rest/json/cves/2.0")
                    .query("pubStartDate={pubStartDate}")
                    .query("pubEndDate={pubEndDate}")
                    .buildAndExpand(
                            String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(startMonth)),
                            String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(startMonth + 1), day)
                    )
                    .toUriString();
        }
        return UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                .path("/rest/json/cves/2.0")
                .query("pubStartDate={pubStartDate}")
                .query("pubEndDate={pubEndDate}")
                .query("startIndex={startIndex}")
                .buildAndExpand(
                        String.format("%s-%s-01T00:00:00.000", year, decimalFormat.format(startMonth)),
                        String.format("%s-%s-%sT00:00:00.000", year, decimalFormat.format(startMonth + 1), day),
                        String.valueOf(startIndex * 2000)
                )
                .toUriString();
    }

}

//                .setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
//                .setHeader("Pragma", "no-cache")
//                .setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
//                .setHeader("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"")
//                .setHeader("Sec-Ch-Ua-Mobile", "?0")
//                .setHeader("Sec-Ch-Ua-Platform", "\"Windows\"")
//                .setHeader("Sec-Fetch-Dest", "document")
//                .setHeader("Sec-Fetch-Mode", "navigate")
//                .setHeader("Sec-Fetch-Site", "none")
//                .setHeader("Sec-Fetch-User", "?1")
//                .setHeader("Upgrade-Insecure-Requests", "1")
//                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")