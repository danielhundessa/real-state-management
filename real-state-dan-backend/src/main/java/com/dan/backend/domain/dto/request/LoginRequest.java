package com.dan.backend.domain.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
