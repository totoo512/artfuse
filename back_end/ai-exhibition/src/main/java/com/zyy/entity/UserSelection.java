package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 用户选择的生成图片记录表
* @TableName user_selection
*/
@Data
public class UserSelection implements Serializable {

    /**
    * 选择记录唯一标识
    */
    @ApiModelProperty("选择记录唯一标识")
    private Long selectionId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 关联用户选中的生成图
    */
    @ApiModelProperty("关联用户选中的生成图")
    private Long genImageId;
    /**
    * 选择时间
    */
    @ApiModelProperty("选择时间")
    private LocalDateTime selectTime;

}
