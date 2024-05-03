package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.UserCookingStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.List;

public interface UserCookingStatisticsRepository extends JpaRepository<UserCookingStatistics, Long> {
  @Query("SELECT new org.springframework.data.util.Pair(FUNCTION('date', ucs.cookedAt), COUNT(ucs)) " +
          "FROM UserCookingStatistics ucs " +
          "WHERE ucs.cookedAt BETWEEN :startDate AND :endDate " +
          "GROUP BY FUNCTION('date', ucs.cookedAt)")
  List<Pair<LocalDateTime, Integer>> countCookingByDay(LocalDateTime startDate, LocalDateTime endDate);

}