package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.resourcemodel.AssetHistory;
import com.certimeter.bff.pagination.AssetHistoryResPagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class AssetHistoryService {
    private final RestTemplate restTemplate;
    private final AssetService assetService;
    @Value("${api.asset.path}")
    private String assetApiUrl;

    public AssetHistoryService(RestTemplate restTemplate, AssetService assetService) {
        this.restTemplate = restTemplate;
        this.assetService = assetService;
    }

    public AssetHistoryResPagination getAllAssetHistories(String accessToken, int pageNo, int pageSize) {
        try {
            HttpHeaders headers = assetService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = String.format("%s?page=%d&pageSize=%d", assetApiUrl + "/history", pageNo, pageSize);
            ResponseEntity<AssetHistoryResPagination> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AssetHistoryResPagination.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, assetService.getAssetApiUrl());
        }
    }

    public AssetHistory getAssetHistory(String accessToken, Long id) {
        try {
            HttpHeaders headers = assetService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = String.format("%s/%d", assetApiUrl + "/history", id);
            ResponseEntity<AssetHistory> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AssetHistory.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, assetService.getAssetApiUrl());
        }
    }
}
