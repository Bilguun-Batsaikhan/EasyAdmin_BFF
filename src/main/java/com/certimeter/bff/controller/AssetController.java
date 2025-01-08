package com.certimeter.bff.controller;

import com.certimeter.bff.dto.AssetDTO;
import com.certimeter.bff.dto.AssetResPagDTO;
import com.certimeter.bff.resourcemodel.Asset;
import com.certimeter.bff.pagination.AssetResPagination;
import com.certimeter.bff.service.AssetService;
import com.certimeter.bff.service.BffService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bff/assets")
public class AssetController {
    private final AssetService assetService;
    private final BffService bffService;
    public AssetController(AssetService assetService, BffService bffService) {
        this.assetService = assetService;
        this.bffService = bffService;
    }

    @GetMapping
    public ResponseEntity<AssetResPagDTO> getAllAssets(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
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
        List<Long> uniqueUserIds = assets.getData().stream().map(Asset::getUserID).distinct().toList();
        Map<Long, String> usernames = bffService.getUsernamesGivenIds(accessTokenTrunked, uniqueUserIds);

        List<AssetDTO> assetDTOs = assets.getData().stream()
                .map(asset -> new AssetDTO(asset.getId(), asset.getModelName(), asset.getType(), asset.getStatus(), asset.getCost(), usernames.get(asset.getUserID())))
                .toList();

        AssetResPagDTO assetResPagDTO = new AssetResPagDTO();
        assetResPagDTO.setData(assetDTOs);
        assetResPagDTO.setPageNo(assets.getPageNo());
        assetResPagDTO.setPageSize(assets.getPageSize());
        assetResPagDTO.setTotalElements(assets.getTotalElements());
        assetResPagDTO.setTotalPages(assets.getTotalPages());
        assetResPagDTO.setLast(assets.isLast());

        return ResponseEntity.ok(assetResPagDTO);
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