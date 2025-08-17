package com.zyy.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置测试控制器
 */
@RestController
@RequestMapping("/admin/config")
@Slf4j
public class ConfigTestController {

    @Autowired
    private MultipartProperties multipartProperties;

    /**
     * 获取当前文件上传配置
     */
    @GetMapping("/multipart")
    public String getMultipartConfig() {
        log.info("当前文件上传配置：");
        log.info("max-file-size: {}", multipartProperties.getMaxFileSize());
        log.info("max-request-size: {}", multipartProperties.getMaxRequestSize());
        log.info("enabled: {}", multipartProperties.isEnabled());
        log.info("file-size-threshold: {}", multipartProperties.getFileSizeThreshold());
        
        return String.format("max-file-size: %s, max-request-size: %s, enabled: %s, file-size-threshold: %s",
                multipartProperties.getMaxFileSize(),
                multipartProperties.getMaxRequestSize(),
                multipartProperties.isEnabled(),
                multipartProperties.getFileSizeThreshold());
    }
}