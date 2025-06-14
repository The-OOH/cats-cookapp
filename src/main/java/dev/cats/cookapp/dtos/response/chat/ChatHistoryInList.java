package dev.cats.cookapp.dtos.response.chat;

import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;

@With
@Builder
public record ChatHistoryInList(
        String chatId,
        String userId,
        LocalDateTime updatedAt
) {

}