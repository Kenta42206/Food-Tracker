package com.example.demo.dto;

import com.example.demo.entity.User;

import lombok.Data;

@Data
public class JwtResponseDto {
    private String token;

    private long expiresIn;

    private User user;
}
