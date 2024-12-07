package com.example.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Food;

public interface  FoodRepository extends JpaRepository<Food, Long> {
    @Query(value = "SELECT * FROM foods f WHERE f.name &@?1", nativeQuery = true)
    Page<Food> findByNameContaining(String name, Pageable pageable);

    // Optional<Food> findById(Long id);

    boolean existsById(long id);
}
