package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Mealhistory;

public interface MealhistoryRepository extends JpaRepository<Mealhistory, Long> {
    @Query("SELECT m FROM Mealhistory m WHERE m.deleteFlg = false and m.userId = :userId AND m.consumedAt BETWEEN :startOfDay AND :endOfDay")
    List<Mealhistory> findByUserIdAndDateRange(
        @Param("userId") Long userId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );

}
