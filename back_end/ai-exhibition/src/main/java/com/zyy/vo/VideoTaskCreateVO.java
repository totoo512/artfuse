package com.zyy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 视频生成任务创建VO
 */
@Data
@ApiModel(description = "视频生成任务创建VO")
public class VideoTaskCreateVO {

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID")
    private Long task_id;

    /**
     * 任务状态：1=排队中，2=处理中，3=完成，4=失败
     */
    @ApiModelProperty(value = "任务状态：1=排队中，2=处理中，3=完成，4=失败")
    private Integer status;
}