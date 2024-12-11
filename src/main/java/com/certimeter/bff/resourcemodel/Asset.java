package com.certimeter.bff.resourcemodel;

import com.certimeter.bff.enumeration.AssetStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Asset {
    private Long id;
    private String modelName;
    private String type;
    private AssetStatus status;
    private BigDecimal cost;
    private Long userID;
}
