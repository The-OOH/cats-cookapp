package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT COUNT(u) FROM User u WHERE u.created_at BETWEEN :startDate AND :endDate")
    Integer countUsersInSeason(Timestamp startDate, Timestamp endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE MONTH(u.created_at) = :month")
    Integer countUsersInMonth(Timestamp month);
}