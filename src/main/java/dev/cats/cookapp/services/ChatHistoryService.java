package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.chat.ChatCompletionResponse;
import dev.cats.cookapp.dtos.response.chat.ChatHistoryInList;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ChatHistoryService {
    List<ChatHistoryInList> getByUserId(String userId);

    ChatCompletionResponse getByConversationId(String userId, String chatId) throws AccessDeniedException;
}
