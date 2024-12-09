package com.example.bff.service;

import com.example.bff.exception.CustomClientException;
import com.example.bff.resourcemodel.User;
import com.example.bff.resourcemodel.UserResPagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Value("${api.user.path}")
    private String userApiUrl;

    private static final String USER_MICROSERVICE_ERROR = "User service is currently unavailable. Please try again later.";

    private final RestTemplate restTemplate;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, USER_MICROSERVICE_ERROR);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, USER_MICROSERVICE_ERROR);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, USER_MICROSERVICE_ERROR);
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
            throw new CustomClientException(HttpStatus.SERVICE_UNAVAILABLE, USER_MICROSERVICE_ERROR);
        }
    }

    private HttpHeaders createHeadersWithToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        return headers;
    }
}