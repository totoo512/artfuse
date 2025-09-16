package com.zyy.config;
import com.zyy.utils.GeminiImageUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gemini配置类
 */
@Configuration
public class GeminiImageConfiguration {
    
    /**
     * 创建Gemini工具类Bean
     */
    @Bean
    @ConditionalOnMissingBean(GeminiImageUtil.class)
    public GeminiImageUtil geminiImageUtil() {
        return new GeminiImageUtil();
    }
}