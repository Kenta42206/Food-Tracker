package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank(message="ユーザー名は必須です")
    @Size(min = 4, max = 255, message = "ユーザー名は４文字以上です。")
    private String username;

    @Email
    @NotBlank(message="メアドは必須です")
    private String email;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 6, max = 255, message = "パスワードは６文字以上です。")
    private String password;

}
