package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dto.response.AdminStatisticsResponse;
import dev.cats.cookapp.dto.response.UserStatisticsResponse;
import dev.cats.cookapp.services.statistic.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("/adminPanel/recipes")
    public AdminStatisticsResponse getAdminRecipesStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return statisticsService.getAdminRecipesStatistics(startDate, endDate);
    }

    @GetMapping("/userPanel/user")
    public AdminStatisticsResponse getAdminUsersStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return statisticsService.getAdminUsersStatistics(startDate, endDate);
    }

    @GetMapping("/userPanel/cooking")
    public UserStatisticsResponse getUserCookingStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        return statisticsService.getUserCookingStatistics(startDate, endDate);
    }
}
