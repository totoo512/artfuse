package com.zyy.service.impl;

import com.zyy.dto.VideoTaskCreateDTO;
import com.zyy.entity.GeneratedImage;
import com.zyy.entity.UploadedImage;
import com.zyy.entity.UserSelection;
import com.zyy.entity.VideoTask;
import com.zyy.exception.BaseException;
import com.zyy.mapper.ImageMapper;
import com.zyy.mapper.VideoMapper;
import com.zyy.service.VideoService;
import com.zyy.context.BaseContext;
import com.zyy.utils.MailUtil;
import com.zyy.utils.VolcengineTosUtil;
import com.zyy.vo.VideoTaskCreateVO;
import com.zyy.vo.VideoTaskQueryVO;
import com.zyy.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 视频生成服务实现类
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;
    
    @Autowired
    private ImageMapper imageMapper;
    
    @Autowired
    private VolcengineTosUtil volcengineTosUtil;
    
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public VideoTaskCreateVO createVideoTask(VideoTaskCreateDTO videoTaskCreateDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 验证selectionId的有效性
        UserSelection userSelection = imageMapper.getUserSelectionById(videoTaskCreateDTO.getSelectionId());
        if (userSelection == null) {
            throw new BaseException("用户选择记录不存在");
        }
        
        // 验证选择记录是否属于当前用户
        if (!userSelection.getUserId().equals(userId)) {
            throw new BaseException("无权限访问该选择记录");
        }
        
        // 验证refImageId的有效性
        UploadedImage refImage = imageMapper.getById(videoTaskCreateDTO.getRefImageId());
        if (refImage == null) {
            throw new BaseException("参考图片不存在");
        }
        
        // 获取生成图片的分析结果作为视频提示词
        GeneratedImage genImage = imageMapper.getGeneratedImageById(userSelection.getGenImageId());
        String videoPrompt = "";
        if (genImage != null && genImage.getAnalysisResult() != null) {
            videoPrompt = genImage.getAnalysisResult();
        }
        
        // 创建视频任务实体
        VideoTask videoTask = new VideoTask();
        videoTask.setUserId(userId);
        videoTask.setSelectionId(videoTaskCreateDTO.getSelectionId());
        videoTask.setRefImageId(videoTaskCreateDTO.getRefImageId());
        videoTask.setVideoPrompt(videoPrompt);
        videoTask.setStatus(1); // 1=排队中
        videoTask.setCreateTime(LocalDateTime.now());
        videoTask.setUpdateTime(LocalDateTime.now());
        
        // 保存到数据库
        videoMapper.insertVideoTask(videoTask);
        
        // 获取图片URL用于WebSocket消息
        String imageUrl1 = "";
        String imageUrl2 = "";
        
        // imageUrl1: 从generated_image表通过genImageId获取
        if (genImage != null) {
            imageUrl1 = genImage.getImageUrl();
        }
        
        // imageUrl2: 从uploaded_image表通过refImageId获取
        imageUrl2 = refImage.getImageUrl();

        // 构建WebSocket消息
        String message = String.format(
            "{\"userId\":\"%d\",\"taskId\":\"%d\",\"imageUrl1\":\"%s\",\"imageUrl2\":\"%s\"}",
            userId, videoTask.getTaskId(), imageUrl1, imageUrl2
        );
        
        // 发送WebSocket消息到所有连接
        try {
            webSocketServer.sendToAllClient(message);
        } catch (Exception e) {
            // WebSocket发送失败不影响主业务流程，只记录日志
            System.err.println("WebSocket消息发送失败: " + e.getMessage());
        }
        
        // 异步发送邮件通知
        sendTaskCreationEmail(videoTask.getTaskId(), userId);
        
        // 构建返回结果
        VideoTaskCreateVO vo = new VideoTaskCreateVO();
        vo.setTaskId(videoTask.getTaskId());
        vo.setStatus(videoTask.getStatus());
        
        return vo;
    }

    @Override
    public VideoTaskQueryVO queryVideoTask(Long taskId) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 根据taskId和userId查询视频任务
        VideoTask videoTask = videoMapper.selectVideoTaskByTaskIdAndUserId(taskId, userId);
        if (videoTask == null) {
            throw new BaseException("视频任务不存在或无权限访问");
        }
        
        // 构建返回结果
        VideoTaskQueryVO vo = new VideoTaskQueryVO();
        vo.setTaskId(videoTask.getTaskId());
        vo.setStatus(videoTask.getStatus());
        vo.setVideoUrl(videoTask.getVideoUrl());
        vo.setErrorMsg(videoTask.getErrorMsg());
        vo.setUpdateTime(videoTask.getUpdateTime());
        
        return vo;
    }

    @Override
    public String updateVideoTaskUrl(Long taskId, MultipartFile file) {
        try {
            // 验证任务存在性
            VideoTask videoTask = videoMapper.selectVideoTaskByTaskId(taskId);
            if (videoTask == null) {
                throw new BaseException("视频任务不存在");
            }
            
            // 生成唯一的文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;
            
            // 上传文件到火山TOS
            String fileUrl = volcengineTosUtil.upload(file.getBytes(), objectName);
            
            // 更新数据库中的videoUrl和status字段
            videoMapper.updateVideoTaskUrl(taskId, fileUrl, 3, LocalDateTime.now());
            
            return fileUrl;
        } catch (Exception e) {
            throw new BaseException("文件上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 异步发送任务创建邮件通知
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    @Async
    public void sendTaskCreationEmail(Long taskId, Long userId) {
        try {
            // 配置收件人邮箱（可以配置为管理员邮箱）
            Set<String> emails = new HashSet<>();
            emails.add("q2777644688@gmail.com"); // 管理员邮箱
            
            // 邮件标题
            String title = "ArtFuse - 新的视频生成任务";
            
            // 邮件内容（HTML格式）
            String content = String.format(
                "<html><body>" +
                "<h3>ArtFuse生成任务创建</h3>" +
                "<p>任务ID: %d</p>" +
                "<p>用户ID: %d</p>" +
                "<p>请访问管理系统查看详情：<a href='http://14.103.244.167/'>http://14.103.244.167/</a></p>" +
                "<p>此邮件由ArtFuse系统自动发送，请勿回复。</p>" +
                "</body></html>",
                taskId, userId
            );
            
            // 发送邮件
            boolean success = MailUtil.sendEmail(emails, title, content);
            if (success) {
                System.out.println("任务创建邮件发送成功，任务ID: " + taskId);
            } else {
                System.err.println("任务创建邮件发送失败，任务ID: " + taskId);
            }
        } catch (Exception e) {
            // 邮件发送失败不影响主业务流程，只记录错误日志
            System.err.println("发送任务创建邮件时发生异常，任务ID: " + taskId + ", 错误: " + e.getMessage());
        }
    }
}