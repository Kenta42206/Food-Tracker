package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Mealhistory;

import jakarta.transaction.Transactional;

public interface MealhistoryRepository extends JpaRepository<Mealhistory, Long> {
    /**
     * 指定されたユーザーIDと消費日時の範囲に基づいて、削除フラグがfalseの食事履歴を取得する。
     * 
     * @param userId ユーザーID
     * @param startOfDay 消費開始日時（その日の開始）
     * @param endOfDay 消費終了日時（その日の終了）
     * @return {@code List<Mealhistory>} 指定された条件に一致する食事履歴のリスト
     */ 
    @Query("SELECT m FROM Mealhistory m WHERE m.deleteFlg = false and m.userId = :userId AND m.consumedAt BETWEEN :startOfDay AND :endOfDay")
    List<Mealhistory> findByUserIdAndDateRange(
        @Param("userId") Long userId,
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay
    );
    
    /**
     * 削除フラグがtrueの食事履歴を一括削除する。
     * 
     * @throws IllegalStateException 削除処理中にエラーが発生した場合
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Mealhistory m WHERE m.deleteFlg = true")
    void deleteMealhistoriesWithDeleteFlg();
}
