package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(nullable=false)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(unique=true, nullable=false)
    private String email;

    @Column(nullable=false)
    private String password;
    
    @Column(name="goal_protein")
    private int goalProtein;

    @Column(name="goal_carbs")
    private int goalCarbs;

    @Column(name="goal_fat")
    private int goalFat;

    @Column(name="goal_calories")
    private int goalCalories;

    @PrePersist
    public void prePersist() {
        this.goalCalories = 2000;
        this.goalProtein = 75;
        this.goalFat = 65;
        this.goalCarbs = 280;
    }

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントが期限切れでない場合
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントがロックされていない場合
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報が期限切れでない場合
    }

    @Override
    public boolean isEnabled() {
        return true; // アカウントが有効な場合
    }

}
