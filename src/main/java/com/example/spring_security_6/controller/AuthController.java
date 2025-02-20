package com.example.spring_security_6.controller;

import com.example.spring_security_6.model.UserLoginDTO;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO request, HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        try {
            Authentication unAuthentication = UsernamePasswordAuthenticationToken.unauthenticated(request.getEmail(), request.getPassword());
            Authentication authenticated = authenticationManager.authenticate(unAuthentication);
            log.info("Authentication Successfully: {}", authenticated);

            // set to SecurityContextHolder to indicate user is authenticated and then save into session
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticated);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, servletRequest, servletResponse);
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Request user to logout: {}", authentication);
        SecurityContextHolder.clearContext();
        securityContextRepository.saveContext(SecurityContextHolder.createEmptyContext(), servletRequest, servletResponse);
        return ResponseEntity.ok("Logout successful");
    }
}
