package com.zyy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 可灵AI配置属性类
 */
@Data
@Component
@ConfigurationProperties(prefix = "zyy.klingai")
public class KelingAiProperties {
    
    /**
     * 访问密钥
     */
    private String accessKey;
    
    /**
     * 秘密密钥
     */
    private String secretKey;
    
    /**
     * API基础URL
     */
    private String baseUrl;
}