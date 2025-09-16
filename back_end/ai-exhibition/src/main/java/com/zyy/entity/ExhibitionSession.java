package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* AI看展模块 - 观展会话表（管理单次观展的所有解读记录）
* @TableName exhibition_session
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionSession implements Serializable {

    /**
    * 会话唯一标识
    */
    @ApiModelProperty("会话唯一标识")
    private Long sessionId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 会话名称（如“2024-09-11 现代艺术展”）
    */
    @ApiModelProperty("会话名称（如“2025-09-11 现代艺术展”）")
    private String sessionName;
    /**
    * 展览背景图片OCR文本（PRD AIE模块可选上传背景图）
    */
    @ApiModelProperty("展览背景图片OCR文本（PRD AIE模块可选上传背景图）")
    private String contextText;
    /**
    * 会话开始时间
    */
    @ApiModelProperty("会话开始时间")
    private LocalDateTime startTime;
    /**
    * 会话结束时间（用户手动结束或1.5小时超时）
    */
    @ApiModelProperty("会话结束时间（用户手动结束或1.5小时超时）")
    private LocalDateTime endTime;
    /**
    * 会话状态：1=进行中，2=已结束
    */
    @ApiModelProperty("会话状态：1=进行中，2=已结束")
    private Integer status;

}
