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

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AssetHistoryService {
    private final RestTemplate restTemplate;
    private final BffService bffService;
    @Value("${api.asset.path}")
    private String assetApiUrl;
    private static Logger LOG = Logger.getLogger(AssetHistoryService.class.getName());

    public AssetHistoryService(RestTemplate restTemplate, BffService bffService) {
        this.restTemplate = restTemplate;
        this.bffService = bffService;
    }

    public AssetHistoryResPagination getAllAssetHistories(String accessToken, int pageNo, int pageSize, Optional<Long> assetId, Optional<String> assetIdMatchMode, Optional<Long> adminId, Optional<String> adminIdMatchMode, Optional<Long> userId, Optional<String> userIdMatchMode, Optional<String> status, Optional<String> statusMatchMode, Optional<String> date, Optional<String> dateMatchMode, Optional<String> action, Optional<String> actionMatchMode) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = buildUrlWithParams(pageNo, pageSize, assetId, assetIdMatchMode, adminId, adminIdMatchMode, userId, userIdMatchMode, status, statusMatchMode, date, dateMatchMode, action, actionMatchMode);
            LOG.info("Request URL: " + url);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, assetApiUrl);
        }
    }

    public AssetHistory getAssetHistory(String accessToken, Long id) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = String.format("%s/history/%d", assetApiUrl, id);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, assetApiUrl);
        }
    }

    private String buildUrlWithParams(int pageNo, int pageSize, Optional<Long> assetId, Optional<String> assetIdMatchMode, Optional<Long> adminId, Optional<String> adminIdMatchMode, Optional<Long> userId, Optional<String> userIdMatchMode, Optional<String> status, Optional<String> statusMatchMode, Optional<String> date, Optional<String> dateMatchMode, Optional<String> action, Optional<String> actionMatchMode) {
        StringBuilder urlBuilder = new StringBuilder(String.format("%s/history?page=%d&pageSize=%d", assetApiUrl, pageNo, pageSize));
        bffService.appendOptionalParam(urlBuilder, "assetId", assetId.map(String::valueOf));
        bffService.appendOptionalParam(urlBuilder, "assetIdMatchMode", assetIdMatchMode);
        bffService.appendOptionalParam(urlBuilder, "adminId", adminId.map(String::valueOf));
        bffService.appendOptionalParam(urlBuilder, "adminIdMatchMode", adminIdMatchMode);
        bffService.appendOptionalParam(urlBuilder, "userId", userId.map(String::valueOf));
        bffService.appendOptionalParam(urlBuilder, "userIdMatchMode", userIdMatchMode);
        bffService.appendOptionalParam(urlBuilder, "status", status);
        bffService.appendOptionalParam(urlBuilder, "statusMatchMode", statusMatchMode);
        bffService.appendOptionalParam(urlBuilder, "date", date);
        bffService.appendOptionalParam(urlBuilder, "dateMatchMode", dateMatchMode);
        bffService.appendOptionalParam(urlBuilder, "action", action);
        bffService.appendOptionalParam(urlBuilder, "actionMatchMode", actionMatchMode);
        return urlBuilder.toString();
    }
}