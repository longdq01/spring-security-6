package com.example.spring_security_6.config;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class RestClientUtils {

    private static final RestClient restClient = RestClient.create();

    public static <T> T post(String url, Object body, Class<T> responseType, Map<String, String> headers){
        return restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .body(body)
                .retrieve()
                .body(responseType);
    }

    public static <T> T get(String url, Map<String, String> headers, Class<T> responseType){
        return restClient.get()
                .uri(url)
                .headers(httpHeaders -> headers.forEach(httpHeaders::set))
                .retrieve()
                .body(responseType);
    }
}
