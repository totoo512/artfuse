package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 用户上传的原始图片表
* @TableName uploaded_image
*/
@Data
public class UploadedImage implements Serializable {

    /**
    * 图片唯一标识
    */
    @ApiModelProperty("图片唯一标识")
    private Long imageId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 图片类型：1=人物照，2=看展照
    */
    @ApiModelProperty("图片类型：1=人物照，2=看展照")
    private Integer imageType;
    /**
    * 图片存储地址（如OSS URL）
    */
    @ApiModelProperty("图片存储地址（如OSS URL）")
    private String imageUrl;
    /**
    * 上传时间
    */
    @ApiModelProperty("上传时间")
    private LocalDateTime uploadTime;

}
