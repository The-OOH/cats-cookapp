package dev.cats.cookapp.services.statistic;

import dev.cats.cookapp.dto.response.AdminStatisticsResponse;
import dev.cats.cookapp.dto.response.UserStatisticsResponse;
import dev.cats.cookapp.repositories.RecipeRepository;
import dev.cats.cookapp.repositories.UserCookingStatisticsRepository;
import dev.cats.cookapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final UserCookingStatisticsRepository userCookingStatisticsRepository;


    private final Map<String, List<LocalDateTime>> seasons = Map.of(
            "winter", List.of(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 3, 31, 23, 59)),
            "spring", List.of(LocalDateTime.of(0, 4, 1, 0, 0), LocalDateTime.of(0, 6, 30, 23, 59)),
            "summer", List.of(LocalDateTime.of(0, 7, 1, 0, 0), LocalDateTime.of(0, 9, 30, 23, 59)),
            "autumn", List.of(LocalDateTime.of(0, 10, 1, 0, 0), LocalDateTime.of(0, 12, 31, 23, 59))
    );

    @Override
    public AdminStatisticsResponse getAdminRecipesStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        boolean isSeasonal = isWithinSingleSeason(startDate, endDate);
        if (isSeasonal) {
            Map<String, Integer> monthlyStatistics = new HashMap<>();
            for (LocalDateTime date = startDate.withHour(0).withMinute(0).withSecond(0).withNano(0);
                 date.isBefore(endDate.plusMonths(1));
                 date = date.withDayOfMonth(1).plusMonths(1)) {

                // Ensure that we don't exceed endDate in the last month of the loop
                if (date.plusMonths(1).isBefore(endDate.plusMonths(1))) {
                    Timestamp startOfMonth = Timestamp.valueOf(date);
                    // Assuming countRecipesInMonth requires a Timestamp at the start of the month
                    monthlyStatistics.put(date.getMonth().name(), recipeRepository.countRecipesInMonth(startOfMonth));
                }
            }
            return new AdminStatisticsResponse(monthlyStatistics.keySet().stream().toList(),
                    monthlyStatistics.values().stream().toList(), true);
        } else {
            Map<String, Integer> recipesCountsBySeason = new HashMap<>();
            seasons.forEach((season, dates) -> {
                LocalDateTime startSeason = startDate.withMonth(dates.get(0).getMonthValue()).withDayOfMonth(dates.get(0)
                        .getDayOfMonth());
                LocalDateTime endSeason = endDate.withMonth(dates.get(1).getMonthValue()).withDayOfMonth(dates.get(1)
                        .getDayOfMonth());
                recipesCountsBySeason.put(season, recipeRepository.countRecipesInSeason(
                        Timestamp.valueOf(startSeason), Timestamp.valueOf(endSeason)));
            });
            return new AdminStatisticsResponse(recipesCountsBySeason.keySet().stream().toList(),
                    recipesCountsBySeason.values().stream().toList(), false);
        }
    }

    private boolean isWithinSingleSeason(LocalDateTime startDate, LocalDateTime endDate) {
        for (List<LocalDateTime> seasonDates : seasons.values()) {
            LocalDateTime startSeason = startDate.withMonth(seasonDates.get(0).getMonthValue()).withDayOfMonth(seasonDates.
                    get(0).getDayOfMonth());
            LocalDateTime endSeason = endDate.withMonth(seasonDates.get(1).getMonthValue()).withDayOfMonth(seasonDates.
                    get(1).getDayOfMonth());
            if (!startDate.isBefore(startSeason) && !endDate.isAfter(endSeason)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public AdminStatisticsResponse getAdminUsersStatistics(LocalDateTime startDate, LocalDateTime endDate){
        boolean isSeasonal = isWithinSingleSeason(startDate, endDate);
        if (isSeasonal) {
            Map<String, Integer> monthlyStatistics = new HashMap<>();
            for (LocalDateTime date = startDate; date.isBefore(endDate.plusMonths(1)); date = date.plusMonths(1)) {
                monthlyStatistics.put(date.getMonth().name(), userRepository.countUsersInMonth(Timestamp.valueOf(date)));
            }
            return new AdminStatisticsResponse(monthlyStatistics.keySet().stream().toList(),
                    monthlyStatistics.values().stream().toList(), true);
        } else {
            Map<String, Integer> usersCountsBySeason = new HashMap<>();
            seasons.forEach((season, dates) -> {
                LocalDateTime startSeason = startDate.withMonth(dates.get(0).getMonthValue()).withDayOfMonth(dates.
                        get(0).getDayOfMonth());
                LocalDateTime endSeason = endDate.withMonth(dates.get(1).getMonthValue()).withDayOfMonth(dates.get(1).
                        getDayOfMonth());
                usersCountsBySeason.put(season, userRepository.countUsersInSeason(Timestamp.valueOf(startSeason),
                        Timestamp.valueOf(endSeason)));
            });
            return new AdminStatisticsResponse(usersCountsBySeason.keySet().stream().toList(),
                    usersCountsBySeason.values().stream().toList(), false);
        }
    }

    @Override
    public UserStatisticsResponse getUserCookingStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        List<Pair<LocalDateTime, Integer>> statistics =
                userCookingStatisticsRepository.countCookingByDay(startDate, endDate);
        return new UserStatisticsResponse(statistics.stream()
                .map(pair -> new UserStatisticsResponse.DataRow(pair.getFirst().toString(), pair.getSecond()))
                .toList());
    }

    @Override
    public LocalDateTime getDateFromISO(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(date, formatter);
    }
}
