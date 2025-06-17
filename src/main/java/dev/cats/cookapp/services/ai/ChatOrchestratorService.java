package dev.cats.cookapp.services.ai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cats.cookapp.config.chatbot.AiChatbotConfig;
import dev.cats.cookapp.dtos.response.chat.ChatMessage;
import dev.cats.cookapp.services.ChatHistoryService;
import dev.cats.cookapp.services.ai.tools.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import dev.cats.cookapp.dtos.request.CompletionRequest;
import dev.cats.cookapp.dtos.response.chat.ChatCompletionResponse;
import dev.cats.cookapp.dtos.response.chat.ChatMessageRole;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatOrchestratorService {
    ChatClient.Builder chatBuilder;
    private static final int WINDOW = 20;
    AiChatbotConfig aiChatbotConfig;
    JdbcChatMemoryRepository memoryRepo;
    ObjectMapper mapper;
    ChatHistoryService chatHistoryService;

    List<ToolCallback> callbackBeans;
    RecipeDetailsTool recipeDetailsTool;
    AIRecipeGenerationTool aIRecipeGenerationTool;
    ImageRecipeGenerationTool imageRecipeGenerationTool;
    VideoRecipeGenerationTool videoRecipeGenerationTool;
    CheckJobStatusTool checkJobStatusTool;

    ToolCallingManager toolCallingManager;


    public ChatCompletionResponse complete(final CompletionRequest req, final String userId) {
        var chatId = this.chatHistoryService.createInitialChat(userId, req.getChatId()).getConversationId();

        final ChatMemory chatMemory = this.memory();
        final var toolCallbacks = this.getCallbackBeans();

        final ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallbacks)
                .internalToolExecutionEnabled(false)
                .toolContext(Map.of("userId", userId))
                .build();

        chatMemory.add(chatId, this.toUserAiMessage(req.getRequest()));
        Prompt promptWithMemory = this.getPrompt(chatMemory.get(chatId), chatOptions);

        ChatResponse chatResponse = this.chatBuilder.build()
                .prompt(promptWithMemory)
                .system(this.aiChatbotConfig.getMainPrompt(userId))
                .toolCallbacks(toolCallbacks)
                .call()
                .chatResponse();

        this.persistAssistant(chatMemory, chatId, chatResponse);

        while (null != chatResponse && chatResponse.hasToolCalls()) {
            final ToolExecutionResult toolExecutionResult = this.toolCallingManager.executeToolCalls(promptWithMemory,
                    chatResponse);
            final ToolResponseMessage lastToolMsg =
                    (ToolResponseMessage) toolExecutionResult.conversationHistory().getLast();

            final var chatMemoryWithToolResponse = chatMemory.get(chatId);
            chatMemoryWithToolResponse.add(chatResponse.getResult().getOutput());
            chatMemoryWithToolResponse.add(lastToolMsg);
            promptWithMemory = this.getPrompt(chatMemoryWithToolResponse, chatOptions);
            chatResponse = this.chatBuilder.build()
                    .prompt(promptWithMemory)
                    .system(this.aiChatbotConfig.getMainPrompt(userId))
                    .toolCallbacks(toolCallbacks)
                    .call()
                    .chatResponse();

            this.persistAssistant(chatMemory, chatId, chatResponse);
        }

        final ChatMessage assistant = this.toChatText(chatResponse);
        return new ChatCompletionResponse(chatId, userId, List.of(assistant), LocalDateTime.now());
    }

    private void persistAssistant(final ChatMemory mem, final String chatId, final ChatResponse resp) {
        if (null == resp) {
            return;
        }
        final String txt = resp.getResult().getOutput().getText();
        if (null != txt && !txt.isBlank()) {
            mem.add(chatId, new AssistantMessage(txt));
        }
    }

    public Flux<ChatCompletionResponse> stream(final CompletionRequest req, final String userId) {
        var chatId = this.chatHistoryService.createInitialChat(userId, req.getChatId()).getConversationId();

        final ChatMemory chatMemory = this.memory();

        final var toolCallbacks = this.getCallbackBeans();

        final ToolCallingChatOptions chatOpts = ToolCallingChatOptions.builder()
                .toolCallbacks(toolCallbacks)
                .internalToolExecutionEnabled(false)
                .toolContext(Map.of("userId", userId))
                .build();

        chatMemory.add(chatId, this.toUserAiMessage(req.getRequest()));

        final ChatClient client = this.chatBuilder.build();

        final StringBuilder fullAnswer = new StringBuilder();

        final Prompt prompt = this.getPrompt(chatMemory.get(chatId), chatOpts);

        final Flux<ChatResponse> rawStream = Flux.defer(() -> this.runStreamOnce(userId, prompt, chatOpts, client))
                .expand(chatResponse -> {
                    if (!chatResponse.hasToolCalls()) return Flux.empty();
                    Prompt promptWithMemory = this.getPrompt(chatMemory.get(chatId), chatOpts);
                    final ToolExecutionResult toolExecutionResult = this.toolCallingManager.executeToolCalls(promptWithMemory,
                            chatResponse);
                    final ToolResponseMessage lastToolMsg =
                            (ToolResponseMessage) toolExecutionResult.conversationHistory().getLast();
                    final var chatMemoryWithToolResponse = chatMemory.get(chatId);
                    chatMemoryWithToolResponse.add(chatResponse.getResult().getOutput());
                    chatMemoryWithToolResponse.add(lastToolMsg);
                    promptWithMemory = new Prompt(chatMemoryWithToolResponse, chatOpts);
                    return this.runStreamOnce(userId, promptWithMemory, chatOpts, client);
                })
                .doOnNext(cr -> fullAnswer.append(cr.getResult().getOutput().getText()));

        return rawStream
                .map(cr -> new ChatCompletionResponse(
                        chatId,
                        userId,
                        List.of(this.toChatMessage(cr)),
                        LocalDateTime.now()
                ))
                .doOnComplete(() -> {
                    final String combined = fullAnswer.toString();
                    if (!combined.isBlank()) {
                        chatMemory.add(chatId, new AssistantMessage(combined));
                    }
                });
    }

    private List<ToolCallback> getCallbackBeans() {
        final var toolCallbacks = new ArrayList<>(Arrays.stream(ToolCallbacks.from(
                this.aIRecipeGenerationTool,
                this.recipeDetailsTool,
                this.imageRecipeGenerationTool,
                this.videoRecipeGenerationTool,
                this.checkJobStatusTool)).toList());
        toolCallbacks.addAll(this.callbackBeans);
        return toolCallbacks;
    }

    private ChatMessage toChatMessage(final ChatResponse cr) {
        return ChatMessage.builder()
                .role(ChatMessageRole.ASSISTANT)
                .content(cr.getResult().getOutput().getText())
                .build();
    }

    private Prompt getPrompt(final List<Message> chatMemory, final ChatOptions chatOpts) {
        return new Prompt(chatMemory.subList(Math.max(0, chatMemory.size() - ChatOrchestratorService.WINDOW), chatMemory.size()), chatOpts);
    }

    private Flux<ChatResponse> runStreamOnce(
            final String userId, final Prompt prompt, final ChatOptions opts, final ChatClient client) {

        return client.prompt(prompt)
                .system(this.aiChatbotConfig.getMainPrompt(userId))
                .toolCallbacks(((ToolCallingChatOptions) opts).getToolCallbacks())
                .stream()
                .chatResponse()
                .filter(cr -> {
                    if (cr.hasToolCalls()) {
                        return true;
                    }
                    final String t = cr.getResult().getOutput().getText();
                    return null != t && !t.isEmpty();
                });
    }

    private ChatMemory memory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(this.memoryRepo)
                .maxMessages(Integer.MAX_VALUE)
                .build();
    }

    private Message toUserAiMessage(final String userMessage) {
        return new UserMessage(userMessage);
    }

    public ChatMessage toChatText(final ChatResponse cr) {
        if (null == cr) {
            return ChatMessage.builder()
                    .role(ChatMessageRole.ASSISTANT)
                    .content(Map.of("message_type", "TEXT", "message", "Sorry, I can't help with this task"))
                    .build();
        }
        final String raw = cr.getResult().getOutput().getText();
        Map<String, Object> content;

        try {
            content = this.mapper.readValue(raw.trim(), Map.class);
        } catch (final JsonProcessingException ex) {
            content = Map.of("message", raw.trim());
        }

        return ChatMessage.builder()
                .role(ChatMessageRole.ASSISTANT)
                .content(content)
                .build();
    }
}
