package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MealhistoryRequestDto;
import com.example.demo.entity.Mealhistory;
import com.example.demo.service.MealhistoryService;




@RestController
@RequestMapping("/api/v1/mealhistory")
public class MealhistoryController {
    @Autowired
    MealhistoryService mealhistoryService;

    @GetMapping("/{date}")
    public ResponseEntity<List<Mealhistory>> getDailyMealhistoryList(@PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        List<Mealhistory> mealhistories = mealhistoryService.getMealhistoriesbyUserIdAndDate(date);
        return new ResponseEntity<>(mealhistories, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Mealhistory> createMealhistory(@RequestBody MealhistoryRequestDto mealhistooryRequestDto) {
        Mealhistory newMealhistory = mealhistoryService.createMealhistory(mealhistooryRequestDto);
        return new ResponseEntity<>(newMealhistory, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Mealhistory>> createMealhistories(@RequestBody List<MealhistoryRequestDto> mealhistoryRequestDto) {
        List<Mealhistory> savedHistories = mealhistoryService.createMealhistories(mealhistoryRequestDto);
        return new ResponseEntity<>(savedHistories, HttpStatus.CREATED);
    }    
}
