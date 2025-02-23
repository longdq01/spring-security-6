package com.example.spring_security_6.controller;

import com.example.spring_security_6.config.Config;
import com.example.spring_security_6.model.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
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
    public ResponseEntity<LoginResDTO> login(@RequestBody UserLoginDTO request) {
        try {
            Authentication unAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());
            Authentication authenticated = authenticationManager.authenticate(unAuthentication);
            log.info("Authentication Successfully: {}", authenticated);
            UserDetailsImpl userDetails = (UserDetailsImpl) authenticated.getPrincipal();

            // set to SecurityContextHolder to indicate user is authenticated and then save into session
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticated);
            SecurityContextHolder.setContext(context);
            SecretKey secretKey = Keys.hmacShaKeyFor(config.getJwtSecretKey().getBytes(StandardCharsets.UTF_8));
            String accessToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(authenticated.getName())
                    .claim("username", authenticated.getName())
                    .claim("authorities", authenticated.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority
                    ).collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + ACCESS_TOKEN_EXPIRATION))
                    .signWith(secretKey).compact();
            String refreshToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(authenticated.getName())
                    .claim("username", authenticated.getName())
                    .claim("authorities", authenticated.getAuthorities().stream().map(
                            GrantedAuthority::getAuthority
                    ).collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date(new Date().getTime() + REFRESH_TOKEN_EXPIRATION))
                    .signWith(secretKey).compact();
            return ResponseEntity.ok(
                    LoginResDTO.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .userInfo(UserInfoDTO.builder()
                                    .id(userDetails.getId())
                                    .email(userDetails.getEmail())
                                    .username(userDetails.getUsername())
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
            String username = String.valueOf(claims.get("username"));
            String authorities = String.valueOf(claims.get("authorities"));
            String accessToken = Jwts.builder().issuer("Eazy Bank")
                    .subject(username)
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
