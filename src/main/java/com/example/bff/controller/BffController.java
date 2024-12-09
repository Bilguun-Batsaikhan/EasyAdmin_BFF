package com.example.bff.controller;


import com.example.bff.resourcemodel.*;
import com.example.bff.service.BffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    //--------------------------//
    //CRUD operations for Asset //
    //--------------------------//
    @GetMapping("/assets")
    public ResponseEntity<AssetResPagination> getAllAssets(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                           @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        AssetResPagination assets = bffService.getAllAssets(accessTokenTrunked, pageNo, pageSize);
        return ResponseEntity.ok(assets);
    }

    @PostMapping("/assets")
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        Asset createdAsset = bffService.createAsset(accessTokenTrunked, asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @PatchMapping("/assets/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        Asset updatedAsset = bffService.updateAsset(accessTokenTrunked, id, updates);
        return ResponseEntity.ok(updatedAsset);
    }

    @DeleteMapping("/assets/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        bffService.removeAsset(accessTokenTrunked, id);
        return ResponseEntity.ok("Asset removed successfully");
    }
    //--------------------------//
    //CRUD operations for Users //
    //--------------------------//
    @GetMapping("/users")
    public ResponseEntity<UserResPagination> getAllUsers(@RequestParam Optional<Integer> age,
                                                         @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return new ResponseEntity<>(bffService.getAllUsers(accessTokenTrunked, age, pageNo, pageSize), HttpStatus.OK);
    }
    @PostMapping("/users")
    public ResponseEntity<String> addUser(@RequestBody User user, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(bffService.addUser(accessTokenTrunked, user));
    }
    @PatchMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(bffService.updateUser(accessTokenTrunked, id, updates));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(bffService.removeUser(accessTokenTrunked, id));
    }
}

