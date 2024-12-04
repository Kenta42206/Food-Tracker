package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

        Mealhistory newMealhistory = mealhistoryRepository.save(new Mealhistory(userId, food, mealhistory.getQuantity()));
        return newMealhistory;
    }

    public Mealhistory updateMealhistory(Long id, MealhistoryRequestDto mealhistooryRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        Mealhistory targetMealhistory = mealhistoryRepository.findById(id).orElseThrow(()->{
            throw new ResourceNotFoundException("Mealhistory not found with id {" + id + "}");
        });

        if(!Objects.equals(userId, targetMealhistory.getUserId())){
            throw new ResourceNotFoundException("Mealhistory not found with id {" + id + "} and user id {" + userId + "}");
        }

        Food food = foodRepository.findById(mealhistooryRequestDto.getFoodId()).orElseThrow(()->{
            throw new ResourceNotFoundException("Food not found with id {" + mealhistooryRequestDto.getFoodId() + "}");
        });

        targetMealhistory.setFood(food);
        targetMealhistory.setQuantity(mealhistooryRequestDto.getQuantity());

        return mealhistoryRepository.save(targetMealhistory);
    }

    public void deleteMealhistory(Long id) {
        if (mealhistoryRepository.existsById(id)) {
            mealhistoryRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Mealhistory not found with id {" + id + "}");
        }
    }
    
}
