package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 视频生成任务状态表
* @TableName video_task
*/
@Data
public class VideoTask implements Serializable {

    /**
    * 任务唯一标识
    */
    @ApiModelProperty("任务唯一标识")
    private Long taskId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 关联用户选择
    */
    @ApiModelProperty("关联用户选择")
    private Long selectionId;
    /**
    * 参考的用户上传图片ID
    */
    @ApiModelProperty("参考的用户上传图片ID")
    private Long refImageId;
    /**
    * 视频生成的核心Prompt
    */
    @ApiModelProperty("视频生成的核心Prompt")
    private String videoPrompt;
    /**
    * 即梦API任务ID
    */
    @ApiModelProperty("即梦API任务ID")
    private String jimengTaskId;
    /**
    * 任务状态：1=排队中，2=处理中，3=完成，4=失败
    */
    @ApiModelProperty("任务状态：1=排队中，2=处理中，3=完成，4=失败")
    private Integer status;
    /**
    * 视频存储地址（完成后填充）
    */
    @ApiModelProperty("视频存储地址（完成后填充）")
    private String videoUrl;
    /**
    * 错误信息（失败时填充）
    */
    @ApiModelProperty("错误信息（失败时填充）")
    private String errorMsg;
    /**
    * 任务创建时间
    */
    @ApiModelProperty("任务创建时间")
    private LocalDateTime createTime;
    /**
    * 任务状态更新时间
    */
    @ApiModelProperty("任务状态更新时间")
    private LocalDateTime updateTime;

}
