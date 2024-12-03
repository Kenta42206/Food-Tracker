package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MealhistoryRequestDto;
import com.example.demo.entity.Food;
import com.example.demo.entity.Mealhistory;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.MealhistoryRepository;

@Service
public class MealhistoryService {
    @Autowired
    MealhistoryRepository mealhistoryRepository;

    @Autowired
    FoodRepository foodRepository;

    public List<Mealhistory> getMealhistoriesbyUserIdAndDate(LocalDate consumedAt){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        List<Mealhistory> mealhistoryList = mealhistoryRepository.findByUserIdAndDate(userId, consumedAt);
        return mealhistoryList;
    }

    public Mealhistory createMealhistory(MealhistoryRequestDto mealhistory){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();


        Long foodId = mealhistory.getFoodId();
        Food food = foodRepository.findById(foodId).orElseThrow(()->{
            throw new ResourceNotFoundException("Food not found with id {" + foodId + "}");
        });

        try{
            Mealhistory newMealhistory = mealhistoryRepository.save(new Mealhistory(userId, food, mealhistory.getQuantity()));
            return newMealhistory;
        } catch(Exception e){
            throw e;
        }
    }
    
}
