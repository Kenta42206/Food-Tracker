package com.example.demo.service;

import java.time.LocalDate;
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
        return reportRepository.findByUserIdAndDate(userId, reportDate).orElseGet(()->{

            Report report = new Report();
            report.setUserId(userId);
            report.setReportDate(reportDate);
            report.setTotalCalories(0);
            report.setTotalProtein(0);
            report.setTotalCarbs(0);
            report.setTotalFat(0);

            return reportRepository.save(report);
        }
        );
    }

    public Report calculateDailyReport(Report report){
        Long userId = report.getUserId();

        List<Mealhistory> mealHistories = mealhistoryRepository.findByUserIdAndDate(userId, report.getReportDate());

        if(mealHistories == null || mealHistories.isEmpty()){
            return report;
        }

        double totalCalories = 0;
        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFat = 0;

        for(Mealhistory mealhistory:mealHistories){
            totalCalories += mealhistory.getQuantity() * mealhistory.getFood().getCalories();
            totalProtein += mealhistory.getQuantity() * mealhistory.getFood().getProtein();
            totalCarbs += mealhistory.getQuantity() * mealhistory.getFood().getCarbs();
            totalFat += mealhistory.getQuantity() * mealhistory.getFood().getFat();
        }

        report.setTotalCalories(totalCalories);
        report.setTotalProtein(totalProtein);
        report.setTotalCarbs(totalCarbs);
        report.setTotalFat(totalFat);

        return report;
    }

    public Report updateDailyReport(LocalDate reportDate){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        Report targetReport = reportRepository.findByUserIdAndDate(userId, reportDate).orElseThrow(()->{
            throw new ResourceNotFoundException("Report not found with user {" + userId + "} and date {" + reportDate + "}");
        });

        return reportRepository.save(calculateDailyReport(targetReport));
    }
}
