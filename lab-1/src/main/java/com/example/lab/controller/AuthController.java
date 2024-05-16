package com.example.lab.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lab.dto.JwtDto;
import com.example.lab.dto.LogInDto;
import com.example.lab.dto.SignUpDto;
import com.example.lab.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @GetMapping("healthcheck")
    public void healthCheck() {
        // Health check for hosting service to know that REST-API is active
    }

    @GetMapping("verify")
    public boolean verify() {
        return true;
    }

    @PostMapping("login")
    public JwtDto login(@RequestBody @Valid LogInDto loginDto) {
        return new JwtDto(service.login(loginDto));
    }
    
    @PostMapping("signup")
    public JwtDto signup(@RequestBody @Valid SignUpDto signupDto) {
        return new JwtDto(service.signup(signupDto));
    }
}
