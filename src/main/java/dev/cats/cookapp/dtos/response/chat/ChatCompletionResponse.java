package dev.cats.cookapp.dtos.response.chat;

import lombok.Builder;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;

@With
@Builder
public record ChatCompletionResponse(
        String chatId,
        String userId,
        List<ChatMessage> messages,
        LocalDateTime updatedAt
) {

}