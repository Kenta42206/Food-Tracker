package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Mealhistory;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MealhistoryRepository;
import com.example.demo.repository.ReportRepository;
import com.example.demo.repository.UserRepository;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MealhistoryRepository mealhistoryRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * ユーザーIDと日付をもとに、その日のレポートを取得する。
     * レポートが存在しない場合は新規作成し、存在する場合は合計値を再計算して返す。
     *
     * @param reportDate レポートの日付
     * @return {@code Report} 該当日のレポート
     * @throws ResourceNotFoundException レポートが見つからない場合
     */
    public Report getDailyReportByUserIdAndDate(LocalDate reportDate){
        Long userId = getUserIdFromPrincipal();

        if(!reportRepository.existsByUserIdAndReportDate(userId,reportDate)){
            return reportRepository.findByUserIdAndDate(userId, reportDate).orElseGet(()->{

                Report report = new Report();
                report.setUserId(userId);
                report.setReportDate(reportDate);
                report.setTotalCalories(0);
                report.setTotalProtein(0);
                report.setTotalCarbs(0);
                report.setTotalFat(0);
    
                return reportRepository.save(report);
            });
        } else{
            Report targetReport = reportRepository.findByUserIdAndDate(userId, reportDate).orElseThrow(()->{
                throw new ResourceNotFoundException("Report not found with user {" + userId + "} and date {" + reportDate + "}");
            });
    
            return reportRepository.save(calculateDailyReport(targetReport));
        }
    }

    /**
     * 選択した日のMealhistoryデータから、合計のプロテイン、脂肪、炭水化物、カロリーを計算する。
     * レポートの日付に関連するMealhistoryを取得し、そのデータをもとに各栄養素の合計を計算して、レポートにセットする。
     *
     * @param report 計算対象のレポート
     * @return {@code Report} 栄養素の合計値をセットしたレポート
     */
    private Report calculateDailyReport(Report report){
        Long userId = report.getUserId();

        LocalDateTime startOfDay = report.getReportDate().atStartOfDay();
        LocalDateTime endOfDay = report.getReportDate().atTime(LocalTime.MAX);
        List<Mealhistory> mealHistories = mealhistoryRepository.findByUserIdAndDateRange(userId, startOfDay, endOfDay);

        if(mealHistories == null || mealHistories.isEmpty()){
            return report;
        }

        int totalCalories = 0;
        int totalProtein = 0;
        int totalCarbs = 0;
        int totalFat = 0;

        for(Mealhistory mealhistory:mealHistories){
            totalCalories += mealhistory.getQuantity() * (mealhistory.getFood().getCalories() /100);
            totalProtein += mealhistory.getQuantity() * (mealhistory.getFood().getProtein() / 100);
            totalCarbs += mealhistory.getQuantity() * (mealhistory.getFood().getCarbs() /100);
            totalFat += mealhistory.getQuantity() * (mealhistory.getFood().getFat() / 100);
        }

        report.setTotalCalories(totalCalories);
        report.setTotalProtein(totalProtein);
        report.setTotalCarbs(totalCarbs);
        report.setTotalFat(totalFat);

        return report;
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
