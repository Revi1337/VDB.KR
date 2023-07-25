package com.revi1337.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revi1337.dto.common.APIResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DecimalFormat;


@Disabled
@SpringBootTest @AutoConfigureMockMvc
class NVCControllerTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RestTemplate restTemplate;

    @Autowired
    public NVCControllerTest(MockMvc mockMvc, RestTemplate restTemplate) {
        this.mockMvc = mockMvc;
        this.restTemplate = restTemplate;
    }

    @Test
    public void fetchCveByIdTest() throws Exception {
        URI uri = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                .path("/rest/json/cves/2.0")
                .query("cveId={cveId}")
                .buildAndExpand("CVE-2019-1010218")
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        String bodyString = responseEntity.getBody();

        APIResponse<String> apiResponse = APIResponse.of(bodyString);
        System.out.println("apiResponse = " + apiResponse);
    }

    @Test
    public void fetchVulnerabilityPOCTest() {
        URI uri = UriComponentsBuilder.fromUriString("https://github.com").path("search")
                .query("q={query}").query("type={type}").query("s={star}").query("o={order}")
                .buildAndExpand("CVE-2017-0144", "repositories", "stars", "desc")
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        String bodyString = responseEntity.getBody();
        System.out.println("bodyString = " + bodyString);
    }

    @Test
    public void decimalFormatTest() {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        for (int number = 0; number <= 12; number++) {
            String format = decimalFormat.format(number);
            System.out.println("format = " + format);
        }
    }

}