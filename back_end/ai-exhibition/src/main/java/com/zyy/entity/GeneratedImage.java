package com.zyy.entity;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* AI生成的风格化图片表
* @TableName generated_image
*/
@Data
public class GeneratedImage implements Serializable {

    /**
    * 生成图片唯一标识
    */
    @ApiModelProperty("生成图片唯一标识")
    private Long genImageId;
    /**
    * 关联分析结果
    */
    @ApiModelProperty("关联分析结果")
    private Long analysisId;
    /**
    * 用于生成视频的分析结果的冗余字段
    */
    @ApiModelProperty("用于生成视频的分析结果的冗余字段")
    private String analysisResult;
    /**
    * 生成图片存储地址
    */
    @ApiModelProperty("生成图片存储地址")
    private String imageUrl;
    /**
    * 生成时间
    */
    @ApiModelProperty("生成时间")
    private Date generateTime;

}
