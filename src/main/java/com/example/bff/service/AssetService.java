package com.example.bff.service;

import com.example.bff.exception.CustomClientException;
import com.example.bff.resourcemodel.Asset;
import com.example.bff.resourcemodel.AssetResPagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AssetService {
    @Value("${api.asset.path}")
    private String assetApiUrl;

    private static final String ASSET_MICROSERVICE_ERROR = "Asset service is currently unavailable. Please try again later.";

    private final RestTemplate restTemplate;

    public AssetService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AssetResPagination getAllAssets(String accessToken, int pageNo, int pageSize) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            String url = String.format("%s?page=%d&pageSize=%d", assetApiUrl, pageNo, pageSize);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, ASSET_MICROSERVICE_ERROR);
        }
    }

    public Asset createAsset(String accessToken, Asset asset) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
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
            HttpHeaders headers = createHeadersWithToken(accessToken);
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
            HttpHeaders headers = createHeadersWithToken(accessToken);
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

    private HttpHeaders createHeadersWithToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }
}