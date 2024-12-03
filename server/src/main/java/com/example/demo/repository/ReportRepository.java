package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Integer>{
    @Query("SELECT r FROM Report r WHERE r.userId = :userId AND DATE(r.reportDate) = :reportDate")
    Optional<Report> findByUserIdAndDate(@Param("userId") Long userId, @Param("reportDate") LocalDate reportDate);

    Report findByUserIdAndReportDate(Long userId, LocalDate reportDate);
}
