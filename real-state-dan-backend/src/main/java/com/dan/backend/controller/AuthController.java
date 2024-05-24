package com.dan.backend.controller;

import com.dan.backend.domain.dto.request.LoginRequest;
import com.dan.backend.domain.dto.request.RegisterRequest;
import com.dan.backend.domain.dto.response.LoginResponse;
import com.dan.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/authenticate")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

}
