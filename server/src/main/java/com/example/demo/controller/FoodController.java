package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.FoodService;


@RestController
@RequestMapping("/api/v1")
public class FoodController {
    @Autowired
    FoodService foodService;

    @GetMapping("/food")
    public ResponseEntity<Map<String, Object>> getMethodName(@RequestParam("name") String name, @RequestParam(name="page", defaultValue="0") int page,@RequestParam(name="size",defaultValue="3") int size) {
        Map<String,Object> response = foodService.getFoodsByNameWithPage(name, page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
