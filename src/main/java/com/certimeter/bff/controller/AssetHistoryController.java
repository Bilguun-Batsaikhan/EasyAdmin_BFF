package com.certimeter.bff.controller;

import com.certimeter.bff.dto.AssetHistoryDTO;
import com.certimeter.bff.dto.AssetHistoryResPagDTO;
import com.certimeter.bff.resourcemodel.AssetHistory;
import com.certimeter.bff.pagination.AssetHistoryResPagination;
import com.certimeter.bff.service.AssetHistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bff/assets/history")
public class AssetHistoryController {
    private final AssetHistoryService assetHistoryService;

    public AssetHistoryController(AssetHistoryService assetHistoryService) {
        this.assetHistoryService = assetHistoryService;
    }

    // TODO add parameters for filtering
    @GetMapping()
    public ResponseEntity<AssetHistoryResPagDTO> getAllAssetHistories(
            @RequestParam Optional<String> assetId, @RequestParam Optional<String> assetIdMatchMode,
            @RequestParam Optional<String> modelName, @RequestParam Optional<String> modelNameMatchMode, @RequestParam Optional<String> admin, @RequestParam Optional<String> adminMatchMode, @RequestParam Optional<String> user, @RequestParam Optional<String> userMatchMode, @RequestParam Optional<String> status, @RequestParam Optional<String> statusMatchMode, @RequestParam Optional<String> date, @RequestParam Optional<String> dateMatchMode, @RequestParam Optional<String> action, @RequestParam Optional<String> actionMatchMode,
            @RequestParam(value = "page", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);

        AssetHistoryResPagination assetHistories = assetHistoryService.getAllAssetHistories(accessTokenTrunked, pageNo, pageSize, assetId, assetIdMatchMode, modelName, modelNameMatchMode, admin, adminMatchMode, user, userMatchMode,status, statusMatchMode, date, dateMatchMode, action, actionMatchMode);

        List<AssetHistoryDTO> assetHistoryDTOs = assetHistories.getData().stream()
                .map(history -> new AssetHistoryDTO(
                        history.getId(),
                        history.getAdmin() != null ? history.getAdmin().getUsername() : null,
                        history.getUser() != null ? history.getUser().getUsername() : null,
                        history.getAsset() != null ? history.getAsset().getModelName() : null,
                        history.getStatus().name(),
                        history.getAction().name(),
                        history.getDate(),
                        history.getComment()
                ))
                .toList();

        AssetHistoryResPagDTO assetHistoryResPagDTO = new AssetHistoryResPagDTO();
        assetHistoryResPagDTO.setData(assetHistoryDTOs);
        assetHistoryResPagDTO.setPageNo(assetHistories.getPageNo());
        assetHistoryResPagDTO.setPageSize(assetHistories.getPageSize());
        assetHistoryResPagDTO.setTotalElements(assetHistories.getTotalElements());
        assetHistoryResPagDTO.setTotalPages(assetHistories.getTotalPages());
        assetHistoryResPagDTO.setLast(assetHistories.isLast());

        return ResponseEntity.ok(assetHistoryResPagDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetHistory> getAssetHistory(@PathVariable Long id, @RequestHeader("Authorization") String accessToken) {
        String accessTokenTrunked = accessToken.substring(7);
        return ResponseEntity.ok(assetHistoryService.getAssetHistory(accessTokenTrunked, id));
    }
}
