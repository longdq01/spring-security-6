package com.example.spring_security_6.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResDTO {

    private String accessToken;
    private UserInfoDTO userInfo;
}
