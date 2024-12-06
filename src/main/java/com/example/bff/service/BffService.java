package com.example.bff.service;

import com.example.bff.exception.CustomClientException;
import com.example.bff.resourcemodel.Asset;
import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class BffService {
    @Value("${api.login.path}")
    private String loginApiUrl;
    @Value("${api.asset.path}")
    private String assetApiUrl;

    private final RestTemplate restTemplate;

    public BffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginResponse> exchange = restTemplate.exchange(loginApiUrl, HttpMethod.POST, entity, LoginResponse.class);
        return exchange.getBody();
    }

    public List<Asset> getAllAssets(String accessToken) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List<Asset>> response = restTemplate.exchange(
                    assetApiUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<Asset>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Log only necessary details for debugging
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            // Create a meaningful exception or response for the user
            throw new CustomClientException(statusCode, errorMessage);
        }
    }


    public Asset createAsset(String accessToken, Asset asset) {
        HttpHeaders headers = createHeadersWithToken(accessToken);
        HttpEntity<Asset> entity = new HttpEntity<>(asset, headers);

        ResponseEntity<Asset> response = restTemplate.exchange(
                assetApiUrl,
                HttpMethod.POST,
                entity,
                Asset.class
        );
        return response.getBody();
    }

    public Asset updateAsset(String accessToken, Long id, Map<String, Object> updates) {
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
    }

    public Asset removeAsset(String accessToken, Long id) {
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
    }

    private HttpHeaders createHeadersWithToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Set the Authorization header with the Bearer token
        return headers;
    }
}
