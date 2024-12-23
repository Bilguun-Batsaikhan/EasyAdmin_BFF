package com.certimeter.bff.controller;

import com.certimeter.bff.resourcemodel.Asset;
import com.certimeter.bff.pagination.AssetResPagination;
import com.certimeter.bff.service.AssetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
                                                           @RequestParam Optional<String> userID,
                                                           @RequestParam Optional<String> userIDMatchMode,
                                                           @RequestParam Optional<String> modelName,
                                                           @RequestParam Optional<String> modelNameMatchMode,
                                                           @RequestParam Optional<String> type,
                                                           @RequestParam Optional<String> typeMatchMode,
                                                           @RequestParam Optional<String> status,
                                                           @RequestParam Optional<String> statusMatchMode,
                                                           @RequestParam Optional<String> cost,
                                                           @RequestParam Optional<String> costMatchMode,
                                                           @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        AssetResPagination assets = assetService.getAllAssets(accessTokenTrunked, pageNo, pageSize, userID, userIDMatchMode, modelName, modelNameMatchMode, type, typeMatchMode, status, statusMatchMode, cost, costMatchMode);
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