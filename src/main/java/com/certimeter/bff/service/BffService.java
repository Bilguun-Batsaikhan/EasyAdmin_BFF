package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.dto.LoginRequest;
import com.certimeter.bff.dto.LoginResponse;
import com.certimeter.bff.dto.RefreshRequest;
import com.certimeter.bff.dto.RefreshResponse;
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

    public RefreshResponse refresh(String accessToken, RefreshRequest refreshRequest) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(accessToken); //TODO: replace with createHeadersWithToken
            HttpEntity<RefreshRequest> entity = new HttpEntity<>(refreshRequest, headers);

            ResponseEntity<RefreshResponse> exchange = restTemplate.exchange(loginApiUrl + "/refresh", HttpMethod.POST, entity, RefreshResponse.class);
            return exchange.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Refresh service is currently unavailable. Please try again later.");
        }
    }

    public Map<Long, String> getUsernamesGivenIds(String accessToken, List<Long> ids) {
        try {
            HttpHeaders headers = createHeadersWithToken(accessToken);
            HttpEntity<List<Long>> entity = new HttpEntity<>(ids, headers);

            ResponseEntity<Map<Long, String>> exchange = restTemplate.exchange(userApiUrl + "/usernames", HttpMethod.POST, entity, new ParameterizedTypeReference<Map<Long, String>>() {
            });
            return exchange.getBody();
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            throw new CustomClientException(statusCode, errorMessage);
        }
    }

    public void appendOptionalParam(StringBuilder urlBuilder, String paramName, Optional<String> paramValue) {
        paramValue.ifPresent(value -> urlBuilder.append("&").append(paramName).append("=").append(value));
    }

    public HttpHeaders createHeadersWithToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }
}