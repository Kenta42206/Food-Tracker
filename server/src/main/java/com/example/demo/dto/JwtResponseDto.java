package com.example.demo.dto;

import lombok.Data;

@Data
public class JwtResponseDto {
    private String token;

    private long expiresIn;
}
