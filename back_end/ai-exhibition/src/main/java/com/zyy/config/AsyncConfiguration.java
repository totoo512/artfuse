package com.zyy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步配置类，启用Spring异步功能
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {
    // 启用异步功能，使用默认的线程池
}