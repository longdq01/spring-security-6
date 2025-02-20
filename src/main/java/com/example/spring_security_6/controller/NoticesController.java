package com.example.spring_security_6.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

    @GetMapping("/notice")
    public String getNotices(){
        return "my notice";
    }
}
