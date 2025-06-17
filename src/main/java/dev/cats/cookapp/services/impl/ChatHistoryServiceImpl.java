package dev.cats.cookapp.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.dtos.response.chat.*;
import dev.cats.cookapp.models.chat.Chat;
import dev.cats.cookapp.repositories.ChatsRepository;
import dev.cats.cookapp.services.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatHistoryServiceImpl implements ChatHistoryService {

    private final ChatsRepository chatsRepository;
    private final JdbcChatMemoryRepository memoryRepository;
    private final ObjectMapper mapper;

    @Value("${chat.default-assistant-message}")
    private String DEFAULT_ASSISTANT_MESSAGE;

    public List<ChatHistoryInList> getByUserId(final String userId) {
        final var chats = this.chatsRepository.findByUserId(userId);
        if (chats.isEmpty()) {
            return List.of();
        }
        return chats.stream()
                .map(chat -> ChatHistoryInList.builder()
                        .chatId(chat.getConversationId())
                        .userId(chat.getUserId())
                        .updatedAt(chat.getUpdatedAt().toLocalDateTime())
                        .build())
                .toList();
    }

    public ChatCompletionResponse getByConversationId(final String userId, final String chatId) throws AccessDeniedException {
        final var chatOptional = this.chatsRepository.findByUserIdAndConversationId(userId, chatId);
        if (chatOptional.isEmpty()) {
            throw new AccessDeniedException("Chat not found or belongs to another user");
        }
        final Chat chat = chatOptional.get();
        final List<Message> messages = this.memoryRepository.findByConversationId(chat.getConversationId());

        return ChatCompletionResponse.builder()
                .chatId(chat.getConversationId())
                .userId(chat.getUserId())
                .messages(messages.stream()
                        .map(this::toChatMessage)
                        .toList())
                .updatedAt(chat.getUpdatedAt().toLocalDateTime())
                .build();
    }

    public ChatCompletionResponse getInitialChat(final String userId) throws AccessDeniedException {
        var chat = createInitialChat(userId, null);
        return getByConversationId(userId, chat.getConversationId());
    }

    public Chat createInitialChat(final String userId, String chatId) {
        if (chatId == null || chatId.isEmpty()) {
            chatId = UUID.randomUUID().toString();
        }
        Chat chat = new Chat(chatId, userId);
        this.chatsRepository.save(chat);

        try {
            var content = Map.of("message", DEFAULT_ASSISTANT_MESSAGE, "messageType", "TEXT");
            var message = new AssistantMessage(this.mapper.writeValueAsString(content));
            var history = memoryRepository.findByConversationId(chat.getConversationId());
            if (history.isEmpty()) {
                memoryRepository.saveAll(chat.getConversationId(), List.of(message));
            }
        }
        catch (final JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return chat;
    }

    private ChatMessage toChatMessage(final Message message) {
        Object content;
        try {
            content = this.mapper.readValue(message.getText(), Object.class);
        } catch (final IOException ex) {
            content = message.getText();
        }
        return ChatMessage.builder()
                .role(ChatMessageRole.valueOf(message.getMessageType().name()))
                .content(content)
                .build();
    }

    @Override
    @Transactional
    public void delete(final String userId, final String chatId) throws AccessDeniedException {
        var conversation = this.chatsRepository.findByUserIdAndConversationId(userId, chatId);
        if (conversation.isEmpty()) {
            throw new AccessDeniedException("Chat not found or belongs to another user");
        }
        var conversationId = conversation.get().getConversationId();
        this.chatsRepository.deleteByConversationId(conversationId);
        this.memoryRepository.deleteByConversationId(conversationId);
    }
}
