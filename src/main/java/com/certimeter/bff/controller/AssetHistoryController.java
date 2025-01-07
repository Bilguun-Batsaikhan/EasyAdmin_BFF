package com.certimeter.bff.controller;

import com.certimeter.bff.resourcemodel.AssetHistory;
import com.certimeter.bff.pagination.AssetHistoryResPagination;
import com.certimeter.bff.service.AssetHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/bff/assets/history")
public class AssetHistoryController {
    private final AssetHistoryService assetHistoryService;

    public AssetHistoryController(AssetHistoryService assetHistoryService) {
        this.assetHistoryService = assetHistoryService;
    }

    @GetMapping
    public ResponseEntity<AssetHistoryResPagination> getAllAssetHistories(@RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
                                                                          @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                          @RequestParam Optional<Long> assetId,
                                                                          @RequestParam Optional<String> assetIdMatchMode,
                                                                          @RequestParam Optional<Long> adminId,
                                                                          @RequestParam Optional<String> adminIdMatchMode,
                                                                          @RequestParam Optional<Long> userId,
                                                                          @RequestParam Optional<String> userIdMatchMode,
                                                                          @RequestParam Optional<String> status,
                                                                          @RequestParam Optional<String> statusMatchMode,
                                                                          @RequestParam Optional<String> date,
                                                                          @RequestParam Optional<String> dateMatchMode,
                                                                          @RequestParam Optional<String> action,
                                                                          @RequestParam Optional<String> actionMatchMode,
                                                                          @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        AssetHistoryResPagination assetHistories = assetHistoryService.getAllAssetHistories(accessTokenTrunked, pageNo, pageSize, assetId, assetIdMatchMode, adminId, adminIdMatchMode, userId, userIdMatchMode, status, statusMatchMode, date, dateMatchMode, action, actionMatchMode);
        return ResponseEntity.ok(assetHistories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetHistory> getAssetHistory(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(assetHistoryService.getAssetHistory(accessTokenTrunked, id));
    }
}
