package com.example.bff.controller;


import com.example.bff.resourcemodel.Asset;
import com.example.bff.resourcemodel.LoginRequest;
import com.example.bff.resourcemodel.LoginResponse;
import com.example.bff.service.BffService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/assets")
    public List<Asset> getAllAssets(@RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return bffService.getAllAssets(accessTokenTrunked);
    }

    @PostMapping("/assets")
    public Asset createAsset(@RequestBody Asset asset, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return bffService.createAsset(accessTokenTrunked, asset);
    }

    @PatchMapping("/assets/{id}")
    public Asset updateAsset(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return bffService.updateAsset(accessTokenTrunked, id, updates);
    }

    @DeleteMapping("/assets/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        bffService.removeAsset(accessTokenTrunked, id);
        return ResponseEntity.ok("Asset removed successfully");
    }
}

