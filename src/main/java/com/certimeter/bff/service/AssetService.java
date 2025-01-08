package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.resourcemodel.Asset;
import com.certimeter.bff.pagination.AssetResPagination;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class AssetService {

    private final BffService bffService;

    @Getter
    @Value("${api.asset.path}")
    private String assetApiUrl;

    public static final String ASSET_MICROSERVICE_ERROR = "Asset service is currently unavailable. Please try again later.";

    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(AssetService.class);
    public AssetService(BffService bffService, RestTemplate restTemplate) {
        this.bffService = bffService;
        this.restTemplate = restTemplate;
    }

    public AssetResPagination getAllAssets(String accessToken, int pageNo, int pageSize, Optional<String> username, Optional<String> usernameMatchMode, Optional<String> modelName, Optional<String> modelNameMatchMode, Optional<String> type, Optional<String> typeMatchMode, Optional<String> status, Optional<String> statusMatchMode, Optional<String> cost, Optional<String> costMatchMode, Optional<String> action, Optional<String> actionMatchMode) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = buildUrlWithParams(pageNo, pageSize, username, usernameMatchMode, modelName, modelNameMatchMode, type, typeMatchMode, status, statusMatchMode, cost, costMatchMode, action, actionMatchMode);
            LOG.info("Request URL: {}", url);

            ResponseEntity<AssetResPagination> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    AssetResPagination.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Asset service is currently unavailable. Please try again later.");
        }
    }

    public Asset createAsset(String accessToken, Asset asset) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Asset> entity = new HttpEntity<>(asset, headers);

            ResponseEntity<Asset> response = restTemplate.exchange(
                    assetApiUrl,
                    HttpMethod.POST,
                    entity,
                    Asset.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, ASSET_MICROSERVICE_ERROR);
        }
    }

    public Asset updateAsset(String accessToken, Long id, Map<String, Object> updates) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);

            String updateUrl = String.format("%s/%d", assetApiUrl, id);
            ResponseEntity<Asset> response = restTemplate.exchange(
                    updateUrl,
                    HttpMethod.PATCH,
                    entity,
                    Asset.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, ASSET_MICROSERVICE_ERROR);
        }
    }

    public Asset removeAsset(String accessToken, Long id) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String deleteUrl = String.format("%s/%d", assetApiUrl, id);
            ResponseEntity<Asset> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    entity,
                    Asset.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, ASSET_MICROSERVICE_ERROR);
        }
    }

    private String buildUrlWithParams(int pageNo, int pageSize, Optional<String> username, Optional<String> usernameMatchMode, Optional<String> modelName, Optional<String> modelNameMatchMode, Optional<String> type, Optional<String> typeMatchMode, Optional<String> status, Optional<String> statusMatchMode, Optional<String> cost, Optional<String> costMatchMode, Optional<String> action, Optional<String> actionMatchMode) {
        StringBuilder urlBuilder = new StringBuilder(String.format("%s?page=%d&pageSize=%d", assetApiUrl, pageNo, pageSize));
        LOG.info("Initial URL: {}", urlBuilder.toString());

        bffService.appendOptionalParam(urlBuilder, "username", username);
        bffService.appendOptionalParam(urlBuilder, "usernameMatchMode", usernameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "modelName", modelName);
        bffService.appendOptionalParam(urlBuilder, "modelNameMatchMode", modelNameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "type", type);
        bffService.appendOptionalParam(urlBuilder, "typeMatchMode", typeMatchMode);
        bffService.appendOptionalParam(urlBuilder, "status", status);
        bffService.appendOptionalParam(urlBuilder, "statusMatchMode", statusMatchMode);
        bffService.appendOptionalParam(urlBuilder, "cost", cost);
        bffService.appendOptionalParam(urlBuilder, "costMatchMode", costMatchMode);
        bffService.appendOptionalParam(urlBuilder, "action", action);
        bffService.appendOptionalParam(urlBuilder, "actionMatchMode", actionMatchMode);

        LOG.info("Final URL: {}", urlBuilder.toString());
        return urlBuilder.toString();
    }
}