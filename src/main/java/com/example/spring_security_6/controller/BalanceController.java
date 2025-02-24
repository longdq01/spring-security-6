package com.example.spring_security_6.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
    @PreAuthorize("hasAuthority('VIEWBALANCE') or hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/balance")
    public String getBalanceDetails(){
        return "my balance";
    }
}
