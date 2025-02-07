package com.certimeter.bff.controller;

import com.certimeter.bff.dto.LoginRequest;
import com.certimeter.bff.dto.LoginResponse;
import com.certimeter.bff.dto.RefreshRequest;
import com.certimeter.bff.dto.RefreshResponse;
import com.certimeter.bff.service.BffService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping(value = "/auth/recovery", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> recovery(@RequestBody Map<String, String> email) {
        bffService.recovery(email);
        return ResponseEntity.ok(Map.of("message", "Recovery email sent."));
    }

    @PostMapping(value = "/auth/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> reset(@RequestHeader("Authorization") String accessToken, @RequestBody Map<String, String> resetRequest) {
        String accessTokenTrunked = accessToken.substring(7);
        bffService.reset(accessTokenTrunked, resetRequest);
        return ResponseEntity.ok(Map.of("message", "Password reset."));
    }
}