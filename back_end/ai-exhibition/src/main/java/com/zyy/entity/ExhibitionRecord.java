package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 艺术足迹模块 - 看展记录表（聚合单次观展的所有信息）
* @TableName exhibition_record
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionRecord implements Serializable {

    /**
    * 看展记录唯一标识
    */
    @ApiModelProperty("看展记录唯一标识")
    private Long recordId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 展览名称（必填，未填则自动生成如“2025-09-11 观展”）
    */
    @ApiModelProperty("展览名称（必填，未填则自动生成如“2025-09-11 观展”）")
    private String title;
    /**
    * 美术馆名称
    */
    @ApiModelProperty("美术馆名称")
    private String galleryName;
    /**
    * 看展日期（必填，默认当前日期）
    */
    @ApiModelProperty("看展日期（必填，默认当前日期）")
    private LocalDateTime exhibitionDate;
    /**
    * 国家
    */
    @ApiModelProperty("国家")
    private String country;
    /**
    * 城市
    */
    @ApiModelProperty("城市")
    private String city;
    /**
    * 主要艺术家（如“毕加索,达利”）
    */
    @ApiModelProperty("主要艺术家（如“毕加索,达利”）")
    private String artist;
    /**
    * 风格标签（逗号分隔，如“立体主义,超现实主义”）
    */
    @ApiModelProperty("风格标签（逗号分隔，如“立体主义,超现实主义”）")
    private String styleTags;
    /**
    * 观展感想（长文本，PRD记录编辑功能）
    */
    @ApiModelProperty("观展感想（长文本，PRD记录编辑功能）")
    private String notes;
    /**
    * AI生成的看展总结（PRD AF模块AI总结功能）
    */
    @ApiModelProperty("AI生成的看展总结（PRD AF模块AI总结功能）")
    private String aiSummary;
    /**
    * 封面图片ID（uploaded_image.image_id，默认第一张图）
    */
    @ApiModelProperty("封面图片url（默认第一张图）")
    private String coverImageUrl;
    /**
    * 记录创建时间
    */
    @ApiModelProperty("记录创建时间")
    private LocalDateTime createTime;
    /**
    * 记录更新时间（支持编辑）
    */
    @ApiModelProperty("记录更新时间（支持编辑）")
    private LocalDateTime updateTime;

}
