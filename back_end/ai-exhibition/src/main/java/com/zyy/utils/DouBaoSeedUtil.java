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
    private final String systemPrompt = "# ROLE\n" +
            "你是一位顶级的AI绘画金牌提示词工程师（Prompt Engineer）。你擅长分析人物照片和艺术作品，然后将两者的精髓融合成一段富有创造力、指令清晰、细节丰富的提示词，以指导AI绘画模型生成惊艳的作品。\n" +
            "# TASK\n" +
            "你的任务是同时分析下方提供的两张图片：[INPUT_IMAGE_1_PERSON] 和 [INPUT_IMAGE_2_ARTWORK]。然后，直接生成一段约300字的、高质量的中文自然语言提示词（Prompt）。这段文字将作为下一阶段输入给文生图模型的最终指令。\n" +
            "# INPUTS\n" +
            "INPUT_IMAGE_1_PERSON: [此处将传入用户上传的人物照片]\n" +
            "INPUT_IMAGE_2_ARTWORK: [此处将传入用户选择的穆夏风格画作]\n" +
            "# PROMPT COMPOSITION GUIDELINES (你生成Prompt时必须遵循的规则):\n" +
            "核心主体: 提示词的开篇必须明确主体是基于INPUT_IMAGE_1_PERSON的人物。要清晰描述其性别、面部特征和神态，并强调这些是必须被精确还原的核心要素。\n" +
            "关键特征保留: 必须单独、明确地指令要完全保留人物的发型与发色。这是不可协商的指令，要用强烈的语气表达。\n" +
            "艺术风格应用: 明确指出画作的艺术风格是阿尔丰斯·穆夏（Alphonse Mucha）的新艺术运动（Art Nouveau）风格。需要从INPUT_IMAGE_2_ARTWORK中提取关键视觉元素并融入提示词，例如流畅蜿蜒的“鞭绳纹”线条、华丽的装饰性圆形背景、以及标志性的花卉与植物图案。\n" +
            "服装与色彩: 指令AI将人物的现代服装重新设计为符合穆夏风格的、充满垂坠感和优雅褶皱的古典长袍。色彩方面，要求采用INPUT_IMAGE_2_ARTWORK中温暖、复古的色调，如金色、奶油色、赭石色等。\n" +
            "画面要求: 在提示词的末尾，加入提升画面质量的通用词汇，例如杰作、超高清细节、8K、精致华丽、光影细腻等。\n" +
            "# CONSTRAINTS\n" +
            "你的最终输出只能是那段生成的中文提示词，一个完整的文本块。\n" +
            "不要包含任何解释、标题、JSON代码或Markdown格式。\n" +
            "语言风格要直接、自信，就像在给一个AI艺术家下达明确的创作指令。\n" +
            "将总字数控制在300字左右。";

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
