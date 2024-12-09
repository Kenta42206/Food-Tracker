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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.dto.MealhistoryRequestDto;
import com.example.demo.entity.Food;
import com.example.demo.entity.Mealhistory;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.FoodRepository;
import com.example.demo.repository.MealhistoryRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class MealhistoryService {
    @Autowired
    MealhistoryRepository mealhistoryRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * 指定した日付に基づいて、ユーザーの食事履歴を取得する。
     * 
     * @param consumedAt 食事を摂取した日付
     * @return {@code List<Mealhistory>} ユーザーの食事履歴リスト
     */
    public List<Mealhistory> getMealhistoriesbyUserIdAndDate(LocalDate consumedAt){
        Long userId = getUserIdFromPrincipal();

        LocalDateTime startOfDay = consumedAt.atStartOfDay();
        LocalDateTime endOfDay = consumedAt.atTime(LocalTime.MAX);
        List<Mealhistory> mealhistoryList = mealhistoryRepository.findByUserIdAndDateRange(userId, startOfDay, endOfDay);
        return mealhistoryList;
    }

    /**
     * 新しい食事履歴を作成する。
     * 
     * @param mealhistory 新しい食事履歴の情報を持つ {@code MealhistoryRequestDto}
     * @return {@code Mealhistory} 作成された食事履歴
     * @throws ResourceNotFoundException 食品IDに該当する食品が見つからなかった場合
     */
    public Mealhistory createMealhistory(MealhistoryRequestDto mealhistory){
        Long userId = getUserIdFromPrincipal();


        Long foodId = mealhistory.getFoodId();
        Food food = foodRepository.findById(foodId).orElseThrow(()->{
            throw new ResourceNotFoundException("Food not found with id {" + foodId + "}");
        });

        Mealhistory newMealhistory = mealhistoryRepository.save(new Mealhistory(userId, food, mealhistory.getQuantity(),mealhistory.getMealNumber(),mealhistory.getConsumedAt(),false));
        return newMealhistory;
    }

    /**
     * 複数の食事履歴を一括で作成・更新する。
     * もし食事履歴のIDが指定されていれば既存の食事履歴を更新し、指定されていなければ新たに作成する。
     * 
     * @param mealhistories 複数の食事履歴情報を持つ {@code List<MealhistoryRequestDto>}
     * @return {@code List<Mealhistory>} 作成または更新された食事履歴リスト
     * @throws ResourceNotFoundException 食品IDに該当する食品が見つからなかった場合
     */
    @Transactional
    public List<Mealhistory> createMealhistories(List<MealhistoryRequestDto> mealhistories){
        Long userId = getUserIdFromPrincipal();

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

    /**
     * 既存の食事履歴を更新する。
     * 
     * @param id 更新対象の食事履歴のID
     * @param mealhistooryRequestDto 更新するための新しい情報を持つ {@code MealhistoryRequestDto}
     * @return {@code Mealhistory} 更新された食事履歴
     * @throws ResourceNotFoundException 食事履歴IDまたは食品IDに該当するレコードが見つからなかった場合
     */
    public Mealhistory updateMealhistory(Long id, MealhistoryRequestDto mealhistooryRequestDto) {
        Long userId = getUserIdFromPrincipal();

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

    /**
     * 指定した食事履歴を削除する。
     * 
     * @param id 削除する食事履歴のID
     * @throws ResourceNotFoundException 食事履歴IDに該当するレコードが見つからなかった場合
     */
    public void deleteMealhistory(Long id) {
        if (mealhistoryRepository.existsById(id)) {
            mealhistoryRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Mealhistory not found with id {" + id + "}");
        }
    }

    /**
     * deleteFlgが立っている食事履歴を一括削除する。
     */
    public void deleteMealhistoriesWithDeleteFlg(){
        mealhistoryRepository.deleteMealhistoriesWithDeleteFlg();
    }

    private Long getUserIdFromPrincipal(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) { 
            User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("User '" + userDetails.getUsername() + "' not found"));
            return user.getId();
        } else {
            throw new IllegalStateException("User details are not of expected type.");
        }
    }
    
}
