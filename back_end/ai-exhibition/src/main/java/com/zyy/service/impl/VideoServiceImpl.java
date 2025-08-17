package com.zyy.service.impl;

import com.zyy.dto.VideoTaskCreateDTO;
import com.zyy.entity.GeneratedImage;
import com.zyy.entity.UserSelection;
import com.zyy.entity.VideoTask;
import com.zyy.exception.BaseException;
import com.zyy.mapper.ImageMapper;
import com.zyy.mapper.VideoMapper;
import com.zyy.service.VideoService;
import com.zyy.context.BaseContext;
import com.zyy.utils.VolcengineTosUtil;
import com.zyy.vo.VideoTaskCreateVO;
import com.zyy.vo.VideoTaskQueryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

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
        GeneratedImage refImage = imageMapper.getGeneratedImageById(videoTaskCreateDTO.getRefImageId());
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
        
        // 构建返回结果
        VideoTaskCreateVO vo = new VideoTaskCreateVO();
        vo.setTask_id(videoTask.getTaskId());
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
}