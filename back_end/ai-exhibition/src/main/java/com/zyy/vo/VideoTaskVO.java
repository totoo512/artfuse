package com.zyy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 视频任务VO，用于管理端查询未完成视频任务
 */
@Data
@ApiModel(description = "视频任务VO")
public class VideoTaskVO {
    
    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private Long taskId;
    
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    
    /**
     * 任务状态：0=未开始，1=排队中，2=处理中，3=完成，4=失败
     */
    @ApiModelProperty(value = "任务状态：0=未开始，1=排队中，2=处理中，3=完成，4=失败")
    private Integer status;
    
    /**
     * 用户选择ID
     */
    @ApiModelProperty(value = "用户选择ID")
    private Long selectionId;
    
    /**
     * 参考图片ID
     */
    @ApiModelProperty(value = "参考图片ID")
    private Long refImageId;
    
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    /**
     * 生成图片URL
     */
    @ApiModelProperty(value = "生成图片URL")
    private String imageUrl1;
    
    /**
     * 参考图片URL
     */
    @ApiModelProperty(value = "参考图片URL")
    private String imageUrl2;
    
    /**
     * 视频生成提示词
     */
    @ApiModelProperty(value = "视频生成提示词")
    private String prompt;
}