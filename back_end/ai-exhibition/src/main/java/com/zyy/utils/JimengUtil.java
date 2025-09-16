package com.zyy.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.volcengine.service.visual.IVisualService;
import com.volcengine.service.visual.impl.VisualServiceImpl;
import com.zyy.config.JimengProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 即梦AI视频生成工具类
 * 基于火山引擎即梦视频3.0 API
 */
@Component
@Slf4j
public class JimengUtil {

    @Autowired
    private JimengProperties jimengProperties;
    
    private IVisualService visualService;
    
    /**
     * 初始化火山引擎视觉服务
     */
    private IVisualService getVisualService() {
        if (visualService == null) {
            visualService = VisualServiceImpl.getInstance();
            // 配置AK/SK
            visualService.setAccessKey(jimengProperties.getAccessKey());
            visualService.setSecretKey(jimengProperties.getSecretKey());
            log.info("火山引擎视觉服务初始化完成");
        }
        return visualService;
    }

    /**
     * 提交视频生成任务（简化版本）
     * @param firstFrameUrl 首帧图片URL
     * @param lastFrameUrl 尾帧图片URL
     * @return 任务结果
     */
    public TaskResult submitTask(String firstFrameUrl, String lastFrameUrl) {
        return submitTask(firstFrameUrl, lastFrameUrl, "", -1, 121);
    }
    
    /**
     * 提交视频生成任务
     * @param firstFrameUrl 首帧图片URL
     * @param lastFrameUrl 尾帧图片URL
     * @param prompt 生成视频的提示词
     * @param seed 随机种子，默认-1
     * @param frames 生成帧数，121(5秒)或241(10秒)
     * @return 任务结果
     */
    public TaskResult submitTask(String firstFrameUrl, String lastFrameUrl, String prompt, Integer seed, Integer frames) {
        List<String> imageUrls = List.of(firstFrameUrl, lastFrameUrl);
        String taskId = submitTaskInternal(imageUrls, prompt, seed, frames);
        
        TaskResult result = new TaskResult();
        result.setTaskId(taskId);
        result.setStatus("submitted");
        result.setSuccess(true);
        return result;
    }
    
    /**
     * 内部提交任务方法
     * @param imageUrls 图片URL列表，需要2张图片（首帧和尾帧）
     * @param prompt 生成视频的提示词
     * @param seed 随机种子，默认-1
     * @param frames 生成帧数，121(5秒)或241(10秒)
     * @return 任务ID
     */
    private String submitTaskInternal(List<String> imageUrls, String prompt, Integer seed, Integer frames) {
        try {
            // 构建请求参数
            JSONObject req = new JSONObject();
            req.put("req_key", jimengProperties.getReqKey());
            req.put("image_urls", imageUrls);
            req.put("prompt", prompt);
            req.put("seed", seed != null ? seed : -1);
            req.put("frames", frames != null ? frames : 121);

            log.info("提交即梦视频生成任务，prompt: {}, frames: {}", prompt, frames);
            
            // 使用火山引擎SDK提交任务
            Object response = getVisualService().cvSync2AsyncSubmitTask(req);
            String responseBody = JSON.toJSONString(response);
            
            log.info("即梦API响应: {}", responseBody);

            JSONObject jsonResponse = JSON.parseObject(responseBody);
            
            // 检查响应状态
            if (jsonResponse.containsKey("code") && jsonResponse.getInteger("code") == 10000) {
                JSONObject data = jsonResponse.getJSONObject("data");
                String taskId = data.getString("task_id");
                log.info("成功提交任务，taskId: {}", taskId);
                return taskId;
            } else {
                String message = jsonResponse.containsKey("message") ? jsonResponse.getString("message") : "未知错误";
                log.error("提交任务失败: {}", message);
                throw new RuntimeException("提交视频生成任务失败: " + message);
            }
            
        } catch (Exception e) {
            log.error("提交即梦视频生成任务失败", e);
            throw new RuntimeException("提交视频生成任务失败", e);
        }
    }

    /**
     * 查询任务结果
     * @param taskId 任务ID
     * @return 任务结果，包含状态和视频URL
     */
    public TaskResult queryTask(String taskId) {
        try {
            // 构建请求参数
            JSONObject req = new JSONObject();
            req.put("req_key", jimengProperties.getReqKey());
            req.put("task_id", taskId);

            log.info("查询即梦视频生成任务结果，taskId: {}", taskId);
            
            // 使用火山引擎SDK查询任务结果
            Object response = getVisualService().cvSync2AsyncGetResult(req);
            String responseBody = JSON.toJSONString(response);
            
            log.info("即梦API查询响应: {}", responseBody);

            JSONObject jsonResponse = JSON.parseObject(responseBody);
            
            // 检查响应状态
            if (jsonResponse.containsKey("code") && jsonResponse.getInteger("code") == 10000) {
                JSONObject dataNode = jsonResponse.getJSONObject("data");
                String status = dataNode.getString("status");
                String videoUrl = dataNode.containsKey("video_url") ? dataNode.getString("video_url") : null;
                
                TaskResult result = new TaskResult();
                result.setStatus(status);
                result.setVideoUrl(videoUrl);
                result.setTaskId(taskId);
                result.setSuccess(true);
                
                log.info("查询任务结果成功，status: {}, videoUrl: {}", status, videoUrl);
                return result;
            } else {
                String message = jsonResponse.containsKey("message") ? jsonResponse.getString("message") : "未知错误";
                log.error("查询任务失败: {}", message);
                
                TaskResult result = new TaskResult();
                result.setTaskId(taskId);
                result.setSuccess(false);
                result.setErrorMessage(message);
                result.setStatus("failed");
                return result;
            }
            
        } catch (Exception e) {
            log.error("查询即梦视频生成任务失败", e);
            
            TaskResult result = new TaskResult();
            result.setTaskId(taskId);
            result.setSuccess(false);
            result.setErrorMessage("查询任务异常: " + e.getMessage());
            result.setStatus("error");
            return result;
        }
    }

    /**
     * 任务结果类
     */
    @Data
    public static class TaskResult {
        private String taskId;
        private String status;
        private String videoUrl;
        private boolean success;
        private String errorMessage;

        /**
         * 判断任务是否完成
         */
        public boolean isDone() {
            return "done".equals(status);
        }

        /**
         * 判断任务是否正在处理
         */
        public boolean isProcessing() {
            return "generating".equals(status) || "in_queue".equals(status);
        }

        /**
         * 判断任务是否失败
         */
        public boolean isFailed() {
            return "not_found".equals(status) || "expired".equals(status);
        }
    }
}