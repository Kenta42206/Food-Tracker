package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Mealhistory;
import com.example.demo.entity.Report;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MealhistoryRepository;
import com.example.demo.repository.ReportRepository;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    @Autowired
    MealhistoryRepository mealhistoryRepository;

    public Report getDailyReportByUserIdAndDate(LocalDate reportDate){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();

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

    public Report calculateDailyReport(Report report){
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
            // (meal.food.protein * (meal.quantity / 100) * 10) / 10
            totalCarbs += mealhistory.getQuantity() * (mealhistory.getFood().getCarbs() /100);
            totalFat += mealhistory.getQuantity() * (mealhistory.getFood().getFat() / 100);
        }

        report.setTotalCalories(totalCalories);
        report.setTotalProtein(totalProtein);
        report.setTotalCarbs(totalCarbs);
        report.setTotalFat(totalFat);

        return report;
    }
}
