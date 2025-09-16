package com.zyy.config;

import com.zyy.properties.KelingAiProperties;
import com.zyy.utils.KelingAiUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 可灵AI配置类
 */
@Configuration
public class KelingAiConfiguration {
    
    /**
     * 创建可灵AI工具类Bean
     */
    @Bean
    @ConditionalOnMissingBean(KelingAiUtil.class)
    public KelingAiUtil kelingAiUtil(KelingAiProperties kelingAiProperties) {
        return new KelingAiUtil(
            kelingAiProperties.getAccessKey(),
            kelingAiProperties.getSecretKey(),
            kelingAiProperties.getBaseUrl(),
            kelingAiProperties.getImageCount()
        );
    }
}