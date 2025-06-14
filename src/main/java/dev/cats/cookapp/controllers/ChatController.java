package dev.cats.cookapp.controllers;

import dev.cats.cookapp.dtos.request.CompletionRequest;
import dev.cats.cookapp.dtos.response.chat.ChatCompletionResponse;
import dev.cats.cookapp.services.ai.ChatOrchestratorService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin(origins = "*")
public class ChatController {
    ChatOrchestratorService chatOrchestratorService;

    @PostMapping
    public ChatCompletionResponse chat(@RequestHeader("x-user-id") final String userId, @RequestBody final CompletionRequest req) {
        return this.chatOrchestratorService.complete(req, userId);
    }

    @PostMapping("/stream")
    public Flux<ChatCompletionResponse> chatStream(@RequestHeader("x-user-id") final String userId, @RequestBody final CompletionRequest req) {
        return this.chatOrchestratorService.stream(req, userId);
    }
}
