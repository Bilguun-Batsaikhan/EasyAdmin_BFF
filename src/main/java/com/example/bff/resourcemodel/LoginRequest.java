package com.example.bff.resourcemodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    @JsonProperty("username")
    private String username; //null
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}

