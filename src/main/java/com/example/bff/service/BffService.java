package com.example.bff.service;

import com.example.bff.exception.CustomClientException;
import com.example.bff.resourcemodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BffService {
    @Value("${api.login.path}")
    private String loginApiUrl;
    @Value("${api.asset.path}")
    private String assetApiUrl;
    @Value("${api.user.path}")
    private String userApiUrl;

    private final RestTemplate restTemplate;

    public BffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

            ResponseEntity<LoginResponse> exchange = restTemplate.exchange(loginApiUrl, HttpMethod.POST, entity, LoginResponse.class);
            return exchange.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Login service is currently unavailable. Please try again later.");
        }
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Asset service is currently unavailable. Please try again later.");
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Asset service is currently unavailable. Please try again later.");
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Asset service is currently unavailable. Please try again later.");
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Asset service is currently unavailable. Please try again later.");
        }
    }

    private HttpHeaders createHeadersWithToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken); // Set the Authorization header with the Bearer token
        return headers;
    }

    public UserResPagination getAllUsers(String accessToken, Optional<Integer> age, int pageNo, int pageSize) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = String.format("%s?page=%d&pageSize=%d", userApiUrl, pageNo, pageSize);
            ResponseEntity<UserResPagination> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    UserResPagination.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "User service is currently unavailable. Please try again later.");
        }
    }

    public String addUser(String accessToken, User user) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<User> entity = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    userApiUrl,
                    HttpMethod.POST,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "User service is currently unavailable. Please try again later.");
        }
    }

    public String updateUser(String accessToken, Long id, Map<String, Object> updates) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(updates, headers);

            String updateUrl = String.format("%s/%d", userApiUrl, id);
            ResponseEntity<String> response = restTemplate.exchange(
                    updateUrl,
                    HttpMethod.PATCH,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "User service is currently unavailable. Please try again later.");
        }
    }

    public String removeUser(String accessToken, Long id) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String deleteUrl = String.format("%s/%d", userApiUrl, id);
            ResponseEntity<String> response = restTemplate.exchange(
                    deleteUrl,
                    HttpMethod.DELETE,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();

            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "User service is currently unavailable. Please try again later.");
        }
    }
}
