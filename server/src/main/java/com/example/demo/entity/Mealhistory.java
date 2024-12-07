package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "meal_history")
@Getter
@Setter
public class Mealhistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false)
    private int quantity; 

    @Column(name="meal_number", nullable=false)
    private int mealNumber;

    @Column(name = "consumed_at", nullable = false)
    private LocalDateTime consumedAt;

    @Column(name="delete_flg")
    private boolean deleteFlg;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Mealhistory(){}
    
    public Mealhistory(Long userId, Food food, int quantity,int mealNumber, LocalDateTime consumedAt,boolean  deleteFlg){
        this.userId = userId;
        this.food = food;
        this.quantity = quantity;
        this.mealNumber = mealNumber;
        this.consumedAt = consumedAt;
        this.deleteFlg = deleteFlg;
    }

}
