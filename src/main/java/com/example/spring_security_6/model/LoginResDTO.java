package com.example.spring_security_6.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResDTO {

    private String accessToken;
    private String refreshToken;
    private UserInfoDTO userInfo;
}
