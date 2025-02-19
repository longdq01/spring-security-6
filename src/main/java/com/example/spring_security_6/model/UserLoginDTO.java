package com.example.spring_security_6.model;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String email;
    private String password;
}
