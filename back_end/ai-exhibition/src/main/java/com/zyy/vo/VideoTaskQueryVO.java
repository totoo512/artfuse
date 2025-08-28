package com.zyy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频任务查询返回对象
 */
@Data
public class VideoTaskQueryVO {
    /**
     * 任务ID
     */
    private Long taskId;
    
    /**
     * 任务状态：1=排队中，2=处理中，3=完成，4=失败
     */
    private Integer status;
    
    /**
     * 视频URL（完成时返回）
     */
    private String videoUrl;
    
    /**
     * 错误信息
     */
    private String errorMsg;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}