package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Report;

public interface ReportRepository extends JpaRepository<Report, Long>{
    /**
     * 指定されたユーザーIDと日付に基づいてレポートを取得する。
     * 
     * @param userId ユーザーID
     * @param reportDate レポートの日付
     * @return {@code Optional<Report>} 指定された条件に一致するレポート（存在しない場合は空）
     */
    @Query("SELECT r FROM Report r WHERE r.userId = :userId AND DATE(r.reportDate) = :reportDate")

    /**
     * 指定されたユーザーIDと日付に基づいてレポートを取得する。
     * 
     * @param userId ユーザーID
     * @param reportDate レポートの日付
     * @return {@code Report} 指定された条件に一致するレポート
     * @throws ResourceNotFoundException レポートが存在しない場合
     */
    Optional<Report> findByUserIdAndDate(@Param("userId") Long userId, @Param("reportDate") LocalDate reportDate);

    /**
     * 指定されたユーザーIDと日付の組み合わせに対するレポートの存在確認を行う。
     * 
     * @param userId ユーザーID
     * @param reportDate レポートの日付
     * @return {@code boolean} レポートが存在する場合はtrue、存在しない場合はfalse
     */
    boolean existsByUserIdAndReportDate(Long userId, LocalDate reportDate);
}
