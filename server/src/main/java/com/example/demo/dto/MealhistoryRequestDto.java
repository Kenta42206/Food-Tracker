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
    @NotNull
    private Long foodId;
    @NotNull
    private int mealNumber;
    @NotNull
    private int quantity;
    @NotBlank
    private LocalDateTime consumedAt;
    private boolean deleteFlg;
}
