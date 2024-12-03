package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealhistoryRequestDto {
    private Long foodId;
    private int quantity;
}
