package dev.cats.cookapp.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@With
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompletionRequest {
    @NotNull
    private String chatId;
    @NotNull
    private String request;
}