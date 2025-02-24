package com.example.spring_security_6.model;

import lombok.Data;

@Data
public class SSOLoginReq {

    private String code;
    private String redirectUri;
}
