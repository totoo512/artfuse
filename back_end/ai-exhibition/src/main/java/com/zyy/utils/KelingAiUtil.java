package com.zyy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 可灵AI工具类
 * 实现多图参考生图API调用
 */
@Slf4j
public class KelingAiUtil {
    
    private final String accessKey;
    private final String secretKey;
    private final String baseUrl;
    private final RestTemplate restTemplate;
    
    public KelingAiUtil(String accessKey, String secretKey, String baseUrl) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
    }
    
    /**
     * 多图参考生图
     * @param portraitImageUrl 人物图片URL
     * @param exhibitionImageUrls 展览图片URL列表
     * @param prompt 提示词
     * @return 生成的图片URL列表
     */
    public List<String> multiImage2Image(String portraitImageUrl, List<String> exhibitionImageUrls, String prompt) {
        try {
            // 1. 创建图片生成任务
            String taskId = createImageGenerationTask(portraitImageUrl, exhibitionImageUrls, prompt);
            log.info("创建可灵AI任务成功，任务ID: {}", taskId);
            
            // 2. 等待任务完成并获取结果
            List<String> imageUrls = waitForTaskCompletion(taskId);
            log.info("可灵AI任务完成，生成图片数量: {}", imageUrls.size());
            
            return imageUrls;
        } catch (Exception e) {
            log.error("可灵AI多图参考生图失败: {}", e.getMessage(), e);
            throw new RuntimeException("可灵AI调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建图片生成任务
     */
    private String createImageGenerationTask(String portraitImageUrl, List<String> exhibitionImageUrls, String prompt) {
        try {
            String url = baseUrl + "/v1/images/multi-image2image";
            
            // 构建请求体
            JSONObject requestBody = new JSONObject();
            requestBody.put("model_name", "kling-v2");
            requestBody.put("prompt", prompt);
            requestBody.put("n", 4); // 生成4张图片
            requestBody.put("aspect_ratio", "3:4");
            
            // 添加主体图片列表（人物图片）
            JSONArray subjectImageList = new JSONArray();
            JSONObject subjectImage1 = new JSONObject();
            subjectImage1.put("subject_image", portraitImageUrl);
            subjectImageList.add(subjectImage1);

            requestBody.put("subject_image_list", subjectImageList);
            
            // 添加参考图片列表（展览图片）
            requestBody.put("style_image", exhibitionImageUrls.getFirst());

            log.info("请求体: {}", requestBody.toString());
            
            // 创建HTTP请求
            HttpHeaders headers = createAuthHeaders("POST", "/v1/images/multi-image2image", requestBody.toString());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);
            
            // 发送请求
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = JSON.parseObject(response.getBody());
                return responseJson.getJSONObject("data").getString("task_id");
            } else {
                throw new RuntimeException("创建任务失败，状态码: " + response.getStatusCode() + ", 响应: " + response.getBody());
            }
        } catch (Exception e) {
            log.error("创建可灵AI任务失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建任务失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 查询任务状态
     */
    public JSONObject queryTaskStatus(String taskId) {
        try {
            String url = baseUrl + "/v1/images/generations/" + taskId;
            
            HttpHeaders headers = createAuthHeaders("GET", "/v1/images/generations/" + taskId, "");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return JSON.parseObject(response.getBody());
            } else {
                throw new RuntimeException("查询任务状态失败，状态码: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("查询可灵AI任务状态失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询任务状态失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 等待任务完成并获取结果
     */
    public List<String> waitForTaskCompletion(String taskId) {
        try {
            int maxAttempts = 60; // 最多等待10分钟
            int attempt = 0;
            
            while (attempt < maxAttempts) {
                JSONObject responseJson = queryTaskStatus(taskId);
                String status = responseJson.getJSONObject("data").getString("task_status");
                String task_status_msg = responseJson.getJSONObject("data").getString("task_status_msg");
                log.info("任务状态查询，任务ID: {}, 状态: {}, 尝试次数: {}", taskId, status, attempt + 1);

                switch (status) {
                    case "succeed" -> {
                        return getTaskResult(taskId);
                    }
                    case "failed" -> throw new RuntimeException("任务执行失败，任务ID: " + taskId + ", 失败原因: " + task_status_msg);
                    case "processing", "submitted" -> {
                        // 任务还在处理中，等待10秒后重试
                        Thread.sleep(10000);
                        attempt++;
                    }
                    case null, default -> throw new RuntimeException("未知任务状态: " + status);
                }
            }
            
            throw new RuntimeException("任务超时，任务ID: " + taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("任务等待被中断", e);
        } catch (Exception e) {
            log.error("等待可灵AI任务完成失败: {}", e.getMessage(), e);
            throw new RuntimeException("等待任务完成失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取任务结果
     */
    private List<String> getTaskResult(String taskId) {
        try {
            String url = baseUrl + "/v1/images/generations/" + taskId;
            
            HttpHeaders headers = createAuthHeaders("GET", "/v1/images/generations/" + taskId, "");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = JSON.parseObject(response.getBody());
                JSONObject dataNode = responseJson.getJSONObject("data");
                JSONArray imagesArray = dataNode.getJSONObject("task_result").getJSONArray("images");
                
                List<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < imagesArray.size(); i++) {
                    JSONObject imageNode = imagesArray.getJSONObject(i);
                    imageUrls.add(imageNode.getString("url"));
                }
                
                return imageUrls;
            } else {
                throw new RuntimeException("获取任务结果失败，状态码: " + response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("获取可灵AI任务结果失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取任务结果失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建认证头
     */
    private HttpHeaders createAuthHeaders(String method, String path, String body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            
            // 生成JWT Token
            String jwtToken = generateJwtToken();
            
            headers.set("Authorization", "Bearer " + jwtToken);
            
            return headers;
        } catch (Exception e) {
            log.error("创建认证头失败: {}", e.getMessage(), e);
            throw new RuntimeException("创建认证头失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 生成JWT Token
     */
    private String generateJwtToken() {
        try {
            long currentTime = System.currentTimeMillis();
            Date expiredAt = new Date(currentTime + TimeUnit.MINUTES.toMillis(30)); // 30分钟有效期
            Date notBefore = new Date(currentTime - TimeUnit.SECONDS.toMillis(5)); // 5秒前开始生效
            
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");
            
            return Jwts.builder()
                    .setIssuer(accessKey) // iss: accessKey
                    .setExpiration(expiredAt) // exp: 过期时间
                    .setNotBefore(notBefore) // nbf: 开始生效时间
                    .setHeader(header)
                    .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                    .compact();
        } catch (Exception e) {
            log.error("生成JWT Token失败: {}", e.getMessage(), e);
            throw new RuntimeException("生成JWT Token失败: " + e.getMessage(), e);
        }
    }
}