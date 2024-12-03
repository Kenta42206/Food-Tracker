package com.example.demo.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Report;
import com.example.demo.service.ReportService;


@RestController
@RequestMapping("/api/v1")
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/report/{date}")
    public ResponseEntity<Report> getMethodName(@PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        Report report = reportService.getDailyReportByUserIdAndDate(date);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @PutMapping("/report/{date}")
    public ResponseEntity<Report> updateDailyReport(@PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date) {
        Report report = reportService.updateDailyReport(date);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}