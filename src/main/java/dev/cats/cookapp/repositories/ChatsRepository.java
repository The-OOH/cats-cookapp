package dev.cats.cookapp.repositories;

import dev.cats.cookapp.models.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatsRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserId(String userId);

    Optional<Chat> findByUserIdAndConversationId(String userId, String conversationId);
}
