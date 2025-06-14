package dev.cats.cookapp.controllers;


import dev.cats.cookapp.dtos.response.chat.ChatCompletionResponse;
import dev.cats.cookapp.dtos.response.chat.ChatHistoryInList;
import dev.cats.cookapp.services.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/chat-history")
@RequiredArgsConstructor
public class ChatHistoryController {

    private final ChatHistoryService chatHistoryService;

    @GetMapping
    public List<ChatHistoryInList> threads(@RequestHeader("x-user-id") final String userId) {
        return this.chatHistoryService.getByUserId(userId);
    }

    @GetMapping("/{chatId}")
    public ChatCompletionResponse thread(@RequestHeader("x-user-id") final String userId,
                                         @PathVariable final String chatId) throws AccessDeniedException {
        return this.chatHistoryService.getByConversationId(userId, chatId);
    }
}
