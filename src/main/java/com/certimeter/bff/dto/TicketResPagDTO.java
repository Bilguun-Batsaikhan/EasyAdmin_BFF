package com.certimeter.bff.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketResPagDTO {
    private List<TicketDTO> data;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}

