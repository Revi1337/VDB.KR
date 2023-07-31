package com.revi1337.controller;

import com.revi1337.dto.common.APIResponse;
import com.revi1337.dto.response.statistics.TotalAttackVectorResponse;
import com.revi1337.dto.response.statistics.TotalByYearResponse;
import com.revi1337.repository.v1.VulnerabilityStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/v1/statistic")
@RestController @RequiredArgsConstructor
public class StatisticsController {

    private final VulnerabilityStatisticsRepository vulnerabilityStatisticsRepository;

    @GetMapping("/year/{year}")
    public APIResponse<?> searchTotalVulnerabilityBySpecifiedYear(@PathVariable int year) {
        TotalByYearResponse totalByYearResponse = vulnerabilityStatisticsRepository
                .searchTotalVulnerabilityBySpecifiedYear(year);
        APIResponse<TotalByYearResponse> apiResponse = APIResponse.of(totalByYearResponse);
        return apiResponse;
    }

    @GetMapping("/years")
    public APIResponse<?> searchTotalVulnerabilityByEachAllYear() {
        List<TotalByYearResponse> totalByYearResponses = vulnerabilityStatisticsRepository
                .searchTotalVulnerabilityByEachAllYear();
        APIResponse<List<TotalByYearResponse>> apiResponse = APIResponse.of(totalByYearResponses);
        return apiResponse;
    }

    @GetMapping("/attack_vector")
    public APIResponse<?> searchExistsAttackVectors() {
        List<String> resultList = vulnerabilityStatisticsRepository.searchExistsAttackVectors();
        APIResponse<List<String>> apiResponse = APIResponse.of(resultList);
        return apiResponse;
    }

    @GetMapping("/attack_vector/{attackVector}")
    public APIResponse<?> searchTotalVulnerabilityBySpecifiedAttackVector(@PathVariable String attackVector) {
        TotalAttackVectorResponse totalAttackVectorResponse = vulnerabilityStatisticsRepository
                .searchTotalVulnerabilityBySpecifiedAttackVector(attackVector);
        APIResponse<TotalAttackVectorResponse> apiResponse = APIResponse.of(totalAttackVectorResponse);
        return apiResponse;
    }

    @GetMapping("/attack_vectors")
    public APIResponse<?> searchTotalVulnerabilityByEachAllAttackVector() {
        List<TotalAttackVectorResponse> totalAttackVectorResponses = vulnerabilityStatisticsRepository
                .searchTotalVulnerabilityByEachAllAttackVector();
        APIResponse<List<TotalAttackVectorResponse>> apiResponse = APIResponse.of(totalAttackVectorResponses);
        return apiResponse;
    }

}
