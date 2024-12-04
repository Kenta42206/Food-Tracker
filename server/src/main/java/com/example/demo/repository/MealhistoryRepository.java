package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Mealhistory;

import jakarta.transaction.Transactional;

public interface MealhistoryRepository extends JpaRepository<Mealhistory, Integer> {
    @Query("SELECT m FROM Mealhistory m WHERE m.userId = :userId AND DATE(m.consumedAt) = :consumedAt")
    List<Mealhistory> findByUserIdAndDate(@Param("userId") Long userId, @Param("consumedAt") LocalDate consumedAt);

    Optional<Mealhistory> findById(Long id);

    boolean existsById(Long id);

    @Transactional
    void deleteById(Long id);
}
