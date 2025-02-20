package com.example.spring_security_6.model;

import lombok.Data;

import java.util.Set;

@Data
public class UserCreateDTO {

    private String username;
    private String password;
    private String email;
    private Set<String> authorities;
}
