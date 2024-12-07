package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

import jakarta.transaction.Transactional;

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

        LocalDateTime startOfDay = consumedAt.atStartOfDay();
        LocalDateTime endOfDay = consumedAt.atTime(LocalTime.MAX);
        List<Mealhistory> mealhistoryList = mealhistoryRepository.findByUserIdAndDateRange(userId, startOfDay, endOfDay);
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

        Mealhistory newMealhistory = mealhistoryRepository.save(new Mealhistory(userId, food, mealhistory.getQuantity(),mealhistory.getMealNumber(),mealhistory.getConsumedAt(),false));
        return newMealhistory;
    }

    @Transactional
    public List<Mealhistory> createMealhistories(List<MealhistoryRequestDto> mealhistories){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

        List<Mealhistory> mealhistoriesToSave = mealhistories.stream()
        .map(mealhistory -> {
            // IDがある場合は既存のレコードを取得して更新
            if(mealhistory.getId() == null){
                return new Mealhistory(
                    userId,
                    foodRepository.findById(mealhistory.getFoodId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                            "Food not found with id {" + mealhistory.getFoodId() + "}")),
                    mealhistory.getQuantity(),
                    mealhistory.getMealNumber(),
                    mealhistory.getConsumedAt(),
                    mealhistory.isDeleteFlg());
            } else{
                Mealhistory existingMealhistory = mealhistoryRepository.findById(mealhistory.getId()).orElse(null);
                existingMealhistory.setFood(foodRepository.findById(mealhistory.getFoodId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Food not found with id {" + mealhistory.getFoodId() + "}")));
            existingMealhistory.setQuantity(mealhistory.getQuantity());
            existingMealhistory.setMealNumber(mealhistory.getMealNumber());
            existingMealhistory.setConsumedAt(mealhistory.getConsumedAt());
            existingMealhistory.setDeleteFlg(mealhistory.isDeleteFlg());
            return existingMealhistory;
            }
        })
        .collect(Collectors.toList());

        return mealhistoryRepository.saveAll(mealhistoriesToSave);
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
