package com.zyy.utils;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionContentPart;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DouBaoSeedUtil {

    private final String apiKey;
    private final String baseUrl;
    private final String model;
    private final String systemPrompt = "";

    private ArkService arkService;
    private ConnectionPool connectionPool;
    private Dispatcher dispatcher;

    public DouBaoSeedUtil(String apiKey, String baseUrl, String model) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    @PostConstruct
    public void init() {
        try {
            connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
            dispatcher = new Dispatcher();
            arkService = ArkService.builder()
                    .dispatcher(dispatcher)
                    .connectionPool(connectionPool)
                    .baseUrl(baseUrl)
                    .apiKey(apiKey)
                    .build();
            log.info("DouBaoSeedUtil initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize DouBaoSeedUtil", e);
            throw new RuntimeException("Failed to initialize DouBaoSeedUtil", e);
        }
    }

    @PreDestroy
    public void destroy() {
        if (arkService != null) {
            arkService.shutdownExecutor();
            log.info("DouBaoSeedUtil destroyed successfully");
        }
    }

    /**
     * 纯文本对话
     * @param prompt 用户输入的文本
     * @return AI回复内容
     */
    public String chat(String prompt) {
        try {
            log.info("Starting text chat with prompt: {}", prompt);
            
            final List<ChatMessage> messages = new ArrayList<>();
            final ChatMessage systemMessage = ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content(systemPrompt)
                    .build();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .content(prompt)
                    .build();
            
            messages.add(systemMessage);
            messages.add(userMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .build();

            String response = (String) arkService.createChatCompletion(chatCompletionRequest)
                    .getChoices().get(0).getMessage().getContent();
            
            log.info("Text chat completed successfully");
            return response;
        } catch (Exception e) {
            log.error("Error during text chat", e);
            throw new RuntimeException("Text chat failed", e);
        }
    }

    /**
     * 图文对话
     * @param prompt 用户输入的文本
     * @param imageUrls 图片URL列表
     * @return AI回复内容
     */
    public String chatWithImages(String prompt, List<String> imageUrls) {
        try {
            log.info("Starting image chat with prompt: {} and {} images", prompt, imageUrls.size());
            
            final List<ChatMessage> messages = new ArrayList<>();
            final List<ChatCompletionContentPart> multiParts = new ArrayList<>();
            
            // 添加图片内容
            for (String imageUrl : imageUrls) {
                multiParts.add(ChatCompletionContentPart.builder()
                        .type("image_url")
                        .imageUrl(new ChatCompletionContentPart.ChatCompletionContentPartImageURL(imageUrl))
                        .build());
            }
            
            // 添加文本内容
            multiParts.add(ChatCompletionContentPart.builder()
                    .type("text")
                    .text(prompt)
                    .build());

            final ChatMessage systemMessage = ChatMessage.builder()
                    .role(ChatMessageRole.SYSTEM)
                    .content(systemPrompt)
                    .build();
            final ChatMessage userMessage = ChatMessage.builder()
                    .role(ChatMessageRole.USER)
                    .multiContent(multiParts)
                    .build();
            
            messages.add(systemMessage);
            messages.add(userMessage);

            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                    .model(model)
                    .messages(messages)
                    .build();

            String response = (String) arkService.createChatCompletion(chatCompletionRequest)
                    .getChoices().getFirst().getMessage().getContent();
            
            log.info("Image chat completed successfully");
            return response;
        } catch (Exception e) {
            log.error("Error during image chat", e);
            throw new RuntimeException("Image chat failed", e);
        }
    }
}
