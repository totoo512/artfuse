package com.zyy.service;

import com.zyy.dto.VideoTaskCreateDTO;
import com.zyy.vo.VideoTaskCreateVO;
import com.zyy.vo.VideoTaskQueryVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频生成服务接口
 */
public interface VideoService {

    /**
     * 创建视频生成任务
     * @param videoTaskCreateDTO 视频任务创建DTO
     * @return 视频任务创建VO
     */
    VideoTaskCreateVO createVideoTask(VideoTaskCreateDTO videoTaskCreateDTO);

    /**
     * 查询视频任务状态
     * @param taskId 任务ID
     * @return 视频任务查询VO
     */
    VideoTaskQueryVO queryVideoTask(Long taskId);

    /**
     * 更新视频任务URL和状态
     * @param taskId 任务ID
     * @param file 上传的视频文件
     * @return 文件URL
     */
    String updateVideoTaskUrl(Long taskId, MultipartFile file);
}