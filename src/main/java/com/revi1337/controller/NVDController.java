package com.revi1337.controller;

import com.revi1337.dto.request.nvd.CVESearchType;
import com.revi1337.dto.response.nvd.CVEResponse;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;


@RestController @RequestMapping("/nvd") @RequiredArgsConstructor
public class NVDController {

    private final RestTemplate restTemplate;

    @GetMapping("/cve")
    public ResponseEntity<?> fetchCveById(CVESearchType cveSearchType) {
        URI uri = UriComponentsBuilder.fromUriString("https://services.nvd.nist.gov")
                .path("/rest/json/cves/2.0")
                .query("cveId={cveId}")
                .buildAndExpand(cveSearchType.cve_id())
                .toUri();

        CVEResponse cveResponse = restTemplate.getForEntity(uri, CVEResponse.class).getBody();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cveResponse);
    }

    @GetMapping("/cve-git")
    public void fetchVulnerabilityPOC(CVESearchType cveSearchType) throws IOException {
        String url = String.format(
                "https://github.com/search?q=%s&type=repositories&s=stars&o=desc", cveSearchType.cve_id()
        );
        Document doc = Jsoup.connect(url).get();
        List<String> gitRepositoryLinks = doc.getElementsByTag("a").stream()
                .filter(element -> element.hasClass("Link--muted"))
                .filter(element -> element.hasAttr("href"))
                .map(hrefLink -> hrefLink.attr("href").replace("/stargazers", ""))
                .map(result -> "https://github.com" + result)
                .toList();

        for (String gitRepositoryLink : gitRepositoryLinks) {
            System.out.println("gitRepositoryLink = " + gitRepositoryLink);
        }
    }
}