package com.example.bff.controller;

import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import com.example.bff.resourcemodel.RefreshRequest;
import com.example.bff.resourcemodel.RefreshResponse;
import com.example.bff.service.BffService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff")
public class BffController {
    private final BffService bffService;

    public BffController(BffService bffService) {
        this.bffService = bffService;
    }

    @PostMapping(value = "/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> loginReq(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(bffService.login(loginRequest));
    }

    @PostMapping(value = "/auth/login/refresh", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RefreshResponse> refresh(@RequestHeader("Authorization") String accessToken, @RequestBody RefreshRequest refreshRequest) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(bffService.refresh(accessTokenTrunked, refreshRequest));
    }
}