package com.example.bff.resourcemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; //Don't forget this!

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshRequest {
    private String refreshToken;
}
