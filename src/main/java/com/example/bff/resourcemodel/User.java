package com.example.bff.resourcemodel;


import com.example.bff.enumeration.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private UserRoleEnum role;
    private String firstname;
    private String surname;
    private LocalDate birthdate;
}

