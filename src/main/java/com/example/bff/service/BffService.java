package com.example.bff.service;

import com.example.bff.exception.CustomClientException;
import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class BffService {
    @Value("${api.login.path}")
    private String loginApiUrl;

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
}