package com.cherlshall.butterfly.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/auth_routes")
    public String login() {
        return "{\"/form/advanced-form\":{\"authority\":[\"admin\",\"user\"]}}";
    }
}
