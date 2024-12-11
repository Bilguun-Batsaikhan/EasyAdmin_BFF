package com.example.bff.resourcemodel;

import lombok.Data;

import java.util.List;
@Data
public class AssetResPagination {
    private List<Asset> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}