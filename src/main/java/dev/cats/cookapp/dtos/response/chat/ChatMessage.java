package dev.cats.cookapp.dtos.response.chat;

import lombok.Builder;
import lombok.With;
import jakarta.validation.constraints.NotNull;

@With
@Builder
public record ChatMessage(
        @NotNull
        ChatMessageRole role,
        @NotNull
        Object content
) {
}