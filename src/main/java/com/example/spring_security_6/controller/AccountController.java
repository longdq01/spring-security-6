package com.example.spring_security_6.controller;

import com.example.spring_security_6.model.UserEntity;
import com.example.spring_security_6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AccountController {

    @GetMapping("/account")
    public String getAccountDetails(){
        return "my account";
    }
}
