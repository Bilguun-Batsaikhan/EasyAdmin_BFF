// AssetHistoryDTO.java
package com.certimeter.bff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetHistoryDTO {
    private Long id;
    private String admin;
    private String user;
    private String modelName;
    private String status;
    private String action;
    private LocalDateTime date;
    private String comment;
}