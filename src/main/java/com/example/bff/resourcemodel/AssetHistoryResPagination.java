package com.example.bff.resourcemodel;

import lombok.Data;

import java.util.List;
@Data
public class AssetHistoryResPagination {
    List<AssetHistory> assetHistoryList;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
