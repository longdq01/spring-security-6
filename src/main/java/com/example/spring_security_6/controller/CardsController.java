package com.example.spring_security_6.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CardsController {

    @GetMapping("/card")
    public String getCardsDetail(){
        return "my card";
    }
}
