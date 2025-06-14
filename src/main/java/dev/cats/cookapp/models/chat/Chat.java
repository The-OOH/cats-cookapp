package dev.cats.cookapp.models.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "chats")
@Getter
@NoArgsConstructor
public class Chat {
    @Id
    private String conversationId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "updated_at", nullable = false)
    private final Timestamp updatedAt = Timestamp.from(Instant.now());

    public Chat(final String conversationId, final String userId) {
        this.conversationId = conversationId;
        this.userId = userId;
    }
}
