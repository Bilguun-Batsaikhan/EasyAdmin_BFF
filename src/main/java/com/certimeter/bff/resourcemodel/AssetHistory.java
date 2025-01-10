package com.certimeter.bff.resourcemodel;

import com.certimeter.bff.enumeration.AssetAction;
import com.certimeter.bff.enumeration.AssetStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class AssetHistory {
    private Long id;
    private Long assetId;
    private Long adminId; // ID of the admin making the change
    private Long userId; // The user this asset is assigned to (if any)
    private AssetStatus status;
    private AssetAction action;
    private LocalDateTime date;
    private String comment;
    private Asset asset; // New relationship
    private User admin;  // New relationship
    private User user;   // New relationship
}
