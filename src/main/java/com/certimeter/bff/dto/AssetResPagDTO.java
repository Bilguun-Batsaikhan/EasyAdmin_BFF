package com.certimeter.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class AssetResPagDTO {
    private List<AssetDTO> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
