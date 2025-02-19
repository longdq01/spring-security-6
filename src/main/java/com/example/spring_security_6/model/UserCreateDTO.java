package com.example.spring_security_6.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserCreateDTO {

    private String username;
    private String password;
    private String roles;
    private String email;
}
