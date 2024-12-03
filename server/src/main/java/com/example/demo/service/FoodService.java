package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Food;
import com.example.demo.repository.FoodRepository;

@Service
public class FoodService {
    @Autowired
    FoodRepository foodRepository;
    
    public Map<String, Object> getFoodsByNameWithPage(String name, int page, int size){
        try {
            List<Food> foods = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Food> pageTuts;

            if(name == null) {
                throw new IllegalArgumentException("Food name must not be empty or null");
            }else{
                pageTuts = foodRepository.findByNameContaining(name, paging);
            }

            foods = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("foods", foods);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());
            return response;
        } catch (IllegalArgumentException  e) {
            throw e;
        }
    }
}
