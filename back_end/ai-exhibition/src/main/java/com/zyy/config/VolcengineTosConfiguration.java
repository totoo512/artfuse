package com.zyy.config;

import com.zyy.properties.VolcengineTosProperties;
import com.zyy.utils.VolcengineTosUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建VolcengineTosUtil的Bean对象
 */
@Configuration
@Slf4j
public class VolcengineTosConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public VolcengineTosUtil VolcengineTosUtil(VolcengineTosProperties volcengineTosProperties) {
        log.info("开始创建VolcengineTosUtil对象：{}", volcengineTosProperties);
        return new VolcengineTosUtil(
                volcengineTosProperties.getRegion(),
                volcengineTosProperties.getEndpoint(),
                volcengineTosProperties.getAccessKey(),
                volcengineTosProperties.getSecretKey(),
                volcengineTosProperties.getBucket()
        );
    }
}
