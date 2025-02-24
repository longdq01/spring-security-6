package com.example.spring_security_6.controller;

import com.example.spring_security_6.config.Config;
import com.example.spring_security_6.config.Constant;
import com.example.spring_security_6.config.RestClientUtils;
import com.example.spring_security_6.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.spring_security_6.config.Constant.ACCESS_TOKEN_EXPIRATION;
import static com.example.spring_security_6.config.Constant.REFRESH_TOKEN_EXPIRATION;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final Config config;

    @PostMapping("/login")
    public ResponseEntity<LoginResDTO> login(@RequestBody SSOLoginReq request) {
        try {
            // get access token
            GetAccessTokenReq reqBody = GetAccessTokenReq.builder()
                    .clientId(config.getSsoClientId())
                    .clientSecret(config.getSsoClientSecret())
                    .code(request.getCode())
                    .redirectUri(request.getRedirectUri())
                    .build();
            Map<String, String> headers1 = new HashMap<>();
            headers1.put(Constant.ACCEPT_HEADER, MediaType.APPLICATION_JSON_VALUE);
            GetAccessTokenRes response = RestClientUtils.post(config.getSsoUrlGetAccessToken(), reqBody, GetAccessTokenRes.class, headers1);
            String accessToken = response.getAccessToken();
            log.info("Get access token successfully: {}", response);

            // get user info
            Map<String, String> headers2 = new HashMap<>();
            headers2.put(Constant.AUTHORIZATION_HEADER, "Bearer " + accessToken);
            UserInfoGithub userInfo = RestClientUtils.get(config.getSsoUrlGetUserInfo(), headers2, UserInfoGithub.class);

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userInfo, Strings.EMPTY));
            UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
            log.info("Authentication Successfully: {}", user);

            SecretKey secretKey = Keys.hmacShaKeyFor(config.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
            String jwtAccessToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("username", user.getUsername())
                    .claim("authorities", user.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority
                    ).collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION))
                    .signWith(secretKey).compact();
            String refreshToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(user.getEmail())
                    .claim("email", user.getEmail())
                    .claim("username", user.getUsername())
                    .claim("authorities", user.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority
                    ).collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRATION))
                    .signWith(secretKey).compact();
            return ResponseEntity.ok(
                    LoginResDTO.builder()
                            .accessToken(jwtAccessToken)
                            .refreshToken(refreshToken)
                            .userInfo(UserInfoDTO.builder()
                                    .id(user.getId())
                                    .email(user.getEmail())
                                    .username(user.getUsername())
                                    .build())
                            .build()
            );
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenReq request) {
        String refreshToken = request.getRefreshToken();
        try{
            SecretKey secretKey = Keys.hmacShaKeyFor(config.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(refreshToken).getPayload();
            String email = String.valueOf(claims.get("email"));
            String username = String.valueOf(claims.get("username"));
            String authorities = String.valueOf(claims.get("authorities"));
            String accessToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(email)
                    .claim("email", email)
                    .claim("username", username)
                    .claim("authorities", authorities)
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION))
                    .signWith(secretKey).compact();;
            return ResponseEntity.ok(accessToken);
        }catch (Exception ex){
            return ResponseEntity.status(400).body("Invalid refresh token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Request user to logout: {}", authentication);
        SecurityContextHolder.clearContext();
        // clear token in redis
        return ResponseEntity.ok("Logout successful");
    }
}
