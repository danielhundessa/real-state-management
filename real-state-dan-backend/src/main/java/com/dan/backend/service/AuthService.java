package com.dan.backend.service;

import com.dan.backend.domain.dto.request.LoginRequest;
import com.dan.backend.domain.dto.request.RegisterRequest;
import com.dan.backend.domain.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);
}
