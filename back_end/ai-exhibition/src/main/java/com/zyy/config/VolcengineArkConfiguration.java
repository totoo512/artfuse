package com.zyy.config;

import com.zyy.properties.VolcengineArkProperties;
import com.zyy.utils.DouBaoSeedUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建DouBaoSeedUtil的Bean对象
 */
@Configuration
@Slf4j
public class VolcengineArkConfiguration {
    @Bean
    @ConditionalOnMissingBean(DouBaoSeedUtil.class)
    public DouBaoSeedUtil douBaoSeedUtil(VolcengineArkProperties volcengineArkProperties) {
        log.info("开始创建DouBaoSeedUtil对象：{}", volcengineArkProperties);
        return new DouBaoSeedUtil(
                volcengineArkProperties.getApiKey(),
                volcengineArkProperties.getBaseUrl(),
                volcengineArkProperties.getModel()
        );
    }
}