package com.certimeter.bff.pagination;

import com.certimeter.bff.resourcemodel.AssetHistory;
import lombok.Data;

import java.util.List;
@Data
public class AssetHistoryResPagination {
    List<AssetHistory> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
