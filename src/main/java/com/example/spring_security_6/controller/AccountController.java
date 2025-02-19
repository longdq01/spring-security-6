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
@RequestMapping("/account")
public class AccountController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

//    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public String getAccountDetails(){
        return "my account";
    }

//    @PostMapping("/register")
//    public ResponseEntity<String> registerUser(@RequestBody UserEntity userEntity){
//        try{
//            String hashPwd = passwordEncoder.encode(userEntity.getPwd());
//            userEntity.setPwd(hashPwd);
//            userRepository.save(userEntity);
//            return ResponseEntity.ok("Given user details are successfully registered");
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred: " + e.getMessage());
//        }
//    }
}
