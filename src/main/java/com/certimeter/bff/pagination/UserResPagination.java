package com.certimeter.bff.pagination;

import com.certimeter.bff.dto.UserDTO;
import lombok.Data;

import java.util.List;

@Data
public class UserResPagination {
    private List<UserDTO> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
