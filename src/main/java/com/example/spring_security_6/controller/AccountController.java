package com.example.spring_security_6.controller;

import com.example.spring_security_6.model.AuthorityEntity;
import com.example.spring_security_6.model.UserCreateDTO;
import com.example.spring_security_6.model.UserEntity;
import com.example.spring_security_6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final UserRepository userRepository;

    @GetMapping("/detail")
    public UserEntity getAccountDetails(@RequestParam String email) {
        UserEntity user = userRepository.findByEmail(email);
        if(user == null){
            return null;
        }else{
            return user;
        }
    }
}
