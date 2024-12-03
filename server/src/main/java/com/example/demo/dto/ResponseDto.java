package com.example.demo.dto;

import lombok.Data;

@Data
public class ResponseDto {
    private String token;

    private long expiresIn;
}
