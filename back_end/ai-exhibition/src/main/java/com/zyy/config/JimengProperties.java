package com.zyy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 即梦AI视频生成配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "zyy.jimeng")
public class JimengProperties {

    /**
     * 访问密钥
     */
    private String accessKey;

    /**
     * 秘密密钥
     */
    private String secretKey;

    /**
     * 区域，默认cn-north-1
     */
    private String region = "cn-north-1";

    /**
     * 服务名，默认cv
     */
    private String service = "cv";

    /**
     * API基础URL
     */
    private String baseUrl = "https://visual.volcengineapi.com";

    /**
     * API版本
     */
    private String version = "2022-08-31";

    /**
     * 请求键
     */
    private String reqKey = "jimeng_i2v_first_tail_v30";
}