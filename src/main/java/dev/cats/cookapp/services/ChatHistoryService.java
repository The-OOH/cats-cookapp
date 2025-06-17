package dev.cats.cookapp.services;

import dev.cats.cookapp.dtos.response.chat.ChatCompletionResponse;
import dev.cats.cookapp.dtos.response.chat.ChatHistoryInList;
import dev.cats.cookapp.models.chat.Chat;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface ChatHistoryService {
    List<ChatHistoryInList> getByUserId(String userId);

    ChatCompletionResponse getByConversationId(String userId, String chatId) throws AccessDeniedException;

    ChatCompletionResponse getInitialChat(String userId) throws AccessDeniedException;

    Chat createInitialChat(String userId, String chatId);

    void delete(String userId, String chatId) throws AccessDeniedException;
}
