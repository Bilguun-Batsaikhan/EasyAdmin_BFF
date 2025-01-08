package com.certimeter.bff.service;

import com.certimeter.bff.exception.CustomClientException;
import com.certimeter.bff.resourcemodel.User;
import com.certimeter.bff.pagination.UserResPagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final BffService bffService;

    @Value("${api.user.path}")
    private String userApiUrl;

    private static final String USER_MICROSERVICE_ERROR = "User service is currently unavailable. Please try again later.";

    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService(BffService bffService, RestTemplate restTemplate) {
        this.bffService = bffService;
        this.restTemplate = restTemplate;
    }

    public UserResPagination getAllUsers(String accessToken, int pageNo, int pageSize, Optional<String> username, Optional<String> usernameMatchMode, Optional<String> firstname, Optional<String> firstnameMatchMode, Optional<String> surname, Optional<String> surnameMatchMode, Optional<String> phoneNumber, Optional<String> phoneNumberMatchMode, Optional<String> email, Optional<String> emailMatchMode, Optional<String> role, Optional<String> roleMatchMode, Optional<String> birthdate, Optional<String> birthdateMatchMode) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = buildUrlWithParams(pageNo, pageSize, username, usernameMatchMode, firstname, firstnameMatchMode, surname, surnameMatchMode, phoneNumber, phoneNumberMatchMode, email, emailMatchMode, role, roleMatchMode, birthdate, birthdateMatchMode);
            LOG.info("Request URL: {}", url);

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

    private String buildUrlWithParams(int pageNo, int pageSize, Optional<String> username, Optional<String> usernameMatchMode, Optional<String> firstname, Optional<String> firstnameMatchMode, Optional<String> surname, Optional<String> surnameMatchMode, Optional<String> phoneNumber, Optional<String> phoneNumberMatchMode, Optional<String> email, Optional<String> emailMatchMode, Optional<String> role, Optional<String> roleMatchMode, Optional<String> birthdate, Optional<String> birthdateMatchMode) {
        StringBuilder urlBuilder = new StringBuilder(String.format("%s?page=%d&pageSize=%d", userApiUrl, pageNo, pageSize));
        bffService.appendOptionalParam(urlBuilder, "username", username);
        bffService.appendOptionalParam(urlBuilder, "usernameMatchMode", usernameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "firstname", firstname);
        bffService.appendOptionalParam(urlBuilder, "firstnameMatchMode", firstnameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "surname", surname);
        bffService.appendOptionalParam(urlBuilder, "surnameMatchMode", surnameMatchMode);
        bffService.appendOptionalParam(urlBuilder, "phoneNumber", phoneNumber);
        bffService.appendOptionalParam(urlBuilder, "phoneNumberMatchMode", phoneNumberMatchMode);
        bffService.appendOptionalParam(urlBuilder, "email", email);
        bffService.appendOptionalParam(urlBuilder, "emailMatchMode", emailMatchMode);
        bffService.appendOptionalParam(urlBuilder, "role", role);
        bffService.appendOptionalParam(urlBuilder, "roleMatchMode", roleMatchMode);
        bffService.appendOptionalParam(urlBuilder, "birthdate", birthdate);
        bffService.appendOptionalParam(urlBuilder, "birthdateMatchMode", birthdateMatchMode);
        return urlBuilder.toString();
    }

    private void appendOptionalParam(StringBuilder urlBuilder, String paramName, Optional<String> paramValue) {
        paramValue.ifPresent(value -> urlBuilder.append("&").append(paramName).append("=").append(value));
    }

    public String addUser(String accessToken, User user) {
        try {
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
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
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
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
            HttpHeaders headers = bffService.createHeadersWithToken(accessToken);
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
}