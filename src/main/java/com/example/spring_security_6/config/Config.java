package com.example.spring_security_6.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class Config {
    @Value(value = "${jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${http.form-login.enable}")
    private boolean formLoginEnabled;

    @Value("${sso.client-id}")
    private String ssoClientId;

    @Value("${sso.client-secret}")
    private String ssoClientSecret;
}
