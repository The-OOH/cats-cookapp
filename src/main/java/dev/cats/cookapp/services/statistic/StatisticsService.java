package dev.cats.cookapp.services.statistic;

import dev.cats.cookapp.dto.response.AdminStatisticsResponse;
import dev.cats.cookapp.dto.response.UserStatisticsResponse;

import java.time.LocalDateTime;

public interface StatisticsService {
    AdminStatisticsResponse getAdminRecipesStatistics(LocalDateTime startDate, LocalDateTime endDate);
    AdminStatisticsResponse getAdminUsersStatistics(LocalDateTime startDate, LocalDateTime endDate);
    UserStatisticsResponse getUserCookingStatistics(LocalDateTime startDate, LocalDateTime endDate);
    LocalDateTime getDateFromISO(String date);
}
