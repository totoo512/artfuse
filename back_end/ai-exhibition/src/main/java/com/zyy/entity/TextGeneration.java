package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* AI生成的图片简介和文案表
* @TableName text_generation
*/
@Data
public class TextGeneration implements Serializable {

    /**
    * 文本结果唯一标识
    */
    @ApiModelProperty("文本结果唯一标识")
    private Long textId;
    /**
    * 关联用户选择
    */
    @ApiModelProperty("关联用户选择")
    private Long selectionId;
    /**
    * 图片简介
    */
    @ApiModelProperty("图片简介")
    private String imageIntro;
    /**
    * 朋友圈文案
    */
    @ApiModelProperty("朋友圈文案")
    private String momentsCopy;
    /**
    * 文本生成时间
    */
    @ApiModelProperty("文本生成时间")
    private LocalDateTime generateTime;

}
