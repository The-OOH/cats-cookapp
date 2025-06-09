package dev.cats.cookapp.repositories;


import dev.cats.cookapp.models.user.token.UserPushToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPushTokenRepository extends JpaRepository<UserPushToken, Long> {
    @Modifying
    @Transactional
    @Query(
            value = """
        INSERT INTO push_tokens (user_id, token, type, platform)
        VALUES (:userId, :token, :type, :platform)
        ON CONFLICT (user_id, token)
        DO UPDATE SET updated_at = NOW()
        """,
            nativeQuery = true
    )
    int upsert(
            @Param("userId") String userId,
            @Param("token") String token,
            @Param("type") String type,
            @Param("platform") String platform
    );
}
