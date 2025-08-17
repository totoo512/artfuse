package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 视频生成任务创建DTO
 */
@Data
@ApiModel(description = "视频生成任务创建DTO")
public class VideoTaskCreateDTO {

    /**
     * 用户选择ID
     */
    @ApiModelProperty(value = "用户选择ID", required = true)
    @NotNull(message = "用户选择ID不能为空")
    private Long selectionId;

    /**
     * 参考图片ID
     */
    @ApiModelProperty(value = "参考图片ID", required = true)
    @NotNull(message = "参考图片ID不能为空")
    private Long refImageId;
}