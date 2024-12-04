package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Food;
import com.example.demo.service.FoodService;


@RestController
@RequestMapping("/api/v1/food")
public class FoodController {
    @Autowired
    FoodService foodService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getFoodsByNameKeywordWithPage(@RequestParam("name") String name, @RequestParam(name="page", defaultValue="0") int page,@RequestParam(name="size",defaultValue="3") int size) {
        Map<String,Object> response = foodService.getFoodsByNameKeywordWithPage(name, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable("id") Long id) {
        Food food = foodService.getFoodById(id);
        return new ResponseEntity<>(food, HttpStatus.OK);
    }
    

}
