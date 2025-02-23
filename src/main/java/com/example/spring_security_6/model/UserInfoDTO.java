package com.example.spring_security_6.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserInfoDTO {

    private String id;
    private String username;
    private String email;
}
