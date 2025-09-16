package com.zyy.utils;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.zyy.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * gemini图片工具类
 */
@Slf4j
public class GeminiImageUtil {

    private static final String MODEL_NAME = "gemini-2.5-flash-image-preview";
    private static final Client client = new Client();
    
    @Autowired
    private VolcengineTosUtil tosUtil;

    /**
     * 多图组合生图
     * @param prompt 生成图片的提示词
     * @param imageUrls 输入图片URL列表
     * @return 生成的图片URL
     */
    public String generateImageFromMultipleImages(String prompt, List<String> imageUrls) {
        try {
            log.info("开始多图组合生图，提示词: {}, 图片数量: {}", prompt, imageUrls != null ? imageUrls.size() : 0);

            List<Part> parts = new ArrayList<>();
            parts.add(Part.fromText(prompt));
            
            // 从网址下载图片并转换为字节数据
            assert imageUrls != null;
            for (String imageUrl : imageUrls) {
                try {
                    log.info("正在下载图片: {}", imageUrl);
                    byte[] imageBytes = downloadImageAsBytes(imageUrl);
                    parts.add(Part.fromBytes(imageBytes, "image/jpeg"));
                    log.info("图片下载完成，大小: {} bytes", imageBytes.length);
                } catch (IOException e) {
                    log.error("下载图片失败: {}, 错误: {}", imageUrl, e.getMessage());
                    throw new RuntimeException("下载图片失败: " + imageUrl, e);
                }
            }

            Content content = Content.builder()
                    .parts(parts)
                    .build();
            // 调用Gemini API生成内容
            GenerateContentResponse response = client.models.generateContent(
                    MODEL_NAME,
                    content, null
            );
            if (response.candidates().isEmpty()) {
                log.error("多图组合生图失败，无候选结果");
                throw new BaseException("多图组合生图失败，无候选结果");
            }

            // 上传生成的图片到TOS云存储
            try {
                for (Part part : response.candidates().get().getFirst().content().get().parts().get()) {
                    if (part.inlineData().isPresent()) {
                        byte[] imageBytes = part.inlineData().get().data().get();
                        log.info("获取生成图片字节数据，大小: {} bytes", imageBytes.length);
                        
                        // 生成唯一的文件名
                        String objectName = UUID.randomUUID() + ".jpg";
                        
                        // 上传到TOS
                        String imageUrl = tosUtil.upload(imageBytes, objectName);
                        log.info("生成图片已上传到TOS: {}", imageUrl);
                        
                        log.info("多图组合生图完成");
                        return imageUrl;
                    }
                }
            } catch (Exception e) {
                log.error("上传生成图片失败: {}", e.getMessage(), e);
                throw new RuntimeException("上传图片失败: " + e.getMessage(), e);
            }
            
            throw new RuntimeException("未找到生成的图片数据");

        } catch (Exception e) {
            log.error("多图组合生图失败: {}", e.getMessage(), e);
            throw new RuntimeException("生图失败: " + e.getMessage(), e);
        }
    }

    /**
     * 从URL下载图片并转换为字节数组
     * @param imageUrl 图片URL
     * @return 图片字节数组
     * @throws IOException 下载失败时抛出异常
     */
    private byte[] downloadImageAsBytes(String imageUrl) throws IOException {
        URL url = URI.create(imageUrl).toURL();
        try (InputStream inputStream = url.openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }
}
