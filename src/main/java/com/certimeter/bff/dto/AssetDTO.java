package com.certimeter.bff.dto;

import com.certimeter.bff.enumeration.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class AssetDTO {
    private Long id;
    private String modelName;
    private String type;
    private AssetStatus status;
    private BigDecimal cost;
    private String username;
}
