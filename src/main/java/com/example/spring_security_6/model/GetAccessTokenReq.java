package com.example.spring_security_6.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAccessTokenReq {
    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "client_secret")
    private String clientSecret;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "redirect_uri")
    private String redirectUri;
}
