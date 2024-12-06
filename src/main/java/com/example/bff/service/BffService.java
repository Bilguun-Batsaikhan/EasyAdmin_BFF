package com.example.bff.service;

import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class BffService {
    @Value("${api.login.path}")
    private String loginApiUrl;
    private final RestTemplate restTemplate;

    public BffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<LoginResponse> exchange = restTemplate.exchange(loginApiUrl, HttpMethod.POST, entity, LoginResponse.class);
        LoginResponse r = exchange.getBody();
        return r;
    }
}
