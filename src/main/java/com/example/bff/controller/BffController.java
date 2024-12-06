package com.example.bff.controller;


import com.example.bff.resourcemodel.Asset;
import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import com.example.bff.service.BffService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/assets")
    public List<Asset> getAllAssets(@RequestHeader("Authorization") String accessToken) {
        return bffService.getAllAssets(accessToken);
    }
}

