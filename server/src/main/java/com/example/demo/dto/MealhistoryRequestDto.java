package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealhistoryRequestDto {
    private Long id;
    @NotNull(message="Foodは必須項目です。")
    private Long foodId;
    @NotNull(message="食事回数は必須項目です。")
    private int mealNumber;
    @NotNull(message="量は必須項目です。")
    private int quantity;
    @NotBlank(message="食事時間は必須項目です。")
    private LocalDateTime consumedAt;
    private boolean deleteFlg;
}
