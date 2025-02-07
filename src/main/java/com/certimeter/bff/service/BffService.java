package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.dto.LoginRequest;
import com.certimeter.bff.dto.LoginResponse;
import com.certimeter.bff.dto.RefreshRequest;
import com.certimeter.bff.dto.RefreshResponse;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void recovery(@RequestBody Map<String, String> email) {
        try {
            // Validate input
            if (!email.containsKey("email") || email.get("email") == null) {
                throw new CustomClientException(HttpStatus.BAD_REQUEST, "Email is missing or invalid.");
            }

            String emailString = email.get("email");
            if (!EmailValidator.getInstance().isValid(emailString)) {
                throw new CustomClientException(HttpStatus.BAD_REQUEST, "Invalid email address.");
            }

            // Forward to backend if valid
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(email, headers);

            restTemplate.exchange("http://localhost:8080/auth/password/recover", HttpMethod.POST, entity, Void.class);
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Recovery service is currently unavailable. Please try again later.");
        }
    }

    public void reset(String accessToken, @RequestBody Map<String, String> reset) {
        try {

            if (!reset.containsKey("password") || reset.get("password") == null) {
                throw new CustomClientException(HttpStatus.BAD_REQUEST, "Password is missing or invalid.");
            }

            // Forward to backend if valid
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(reset, headers);

            restTemplate.exchange("http://localhost:8080/auth/password/reset", HttpMethod.PATCH, entity, Void.class);
        } catch (HttpClientErrorException e) {
            String errorMessage = e.getResponseBodyAsString();
            HttpStatusCode statusCode = e.getStatusCode();
            throw new CustomClientException(statusCode, errorMessage);
        } catch (ResourceAccessException e) {
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, "Reset service is currently unavailable. Please try again later.");
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