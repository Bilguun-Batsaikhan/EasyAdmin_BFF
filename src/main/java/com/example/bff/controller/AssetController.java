package com.example.bff.controller;

import com.example.bff.resourcemodel.Asset;
import com.example.bff.resourcemodel.AssetResPagination;
import com.example.bff.service.AssetService;
import com.example.bff.service.BffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bff/assets")
public class AssetController {
    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    public ResponseEntity<AssetResPagination> getAllAssets(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                           @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                           @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        AssetResPagination assets = assetService.getAllAssets(accessTokenTrunked, pageNo, pageSize);
        return ResponseEntity.ok(assets);
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        Asset createdAsset = assetService.createAsset(accessTokenTrunked, asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAsset);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable Long id, @RequestBody Map<String, Object> updates, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        Asset updatedAsset = assetService.updateAsset(accessTokenTrunked, id, updates);
        return ResponseEntity.ok(updatedAsset);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        assetService.removeAsset(accessTokenTrunked, id);
        return ResponseEntity.ok("Asset removed successfully");
    }
}