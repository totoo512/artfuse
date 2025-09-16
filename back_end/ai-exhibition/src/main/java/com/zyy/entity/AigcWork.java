package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 创作中心模块 - AI生成风格化图片表（PRD核心转化路径数据）
* @TableName generated_image
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeneratedImage implements Serializable {

    /**
    * 生成图片唯一标识
    */
    @ApiModelProperty("生成图片唯一标识")
    private Long genImageId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
     * 展览记录id
     */
    @ApiModelProperty("展览记录id")
    private Long exhibitionRecordId;
    /**
    * 风格ID
    */
    @ApiModelProperty("风格ID")
    private Integer styleId;
    /**
    * AIGC创作类型：1=明信片图鉴，2=艺术贴纸，3=定制冰箱贴，4=魔法照片和视频
    */
    @ApiModelProperty("AIGC创作类型：1=明信片图鉴，2=艺术贴纸，3=定制冰箱贴，4=魔法照片和视频")
    private Integer creationType;
    /**
    * 源图片ID列表（逗号分隔，关联uploaded_image.image_id，PRD多选素材需求）
    */
    @ApiModelProperty("源图片ID列表（逗号分隔，关联uploaded_image.image_id，PRD多选素材需求）")
    private String sourceImageIds;
    /**
    * 作品名称（用户自定义，可选）
    */
    @ApiModelProperty("作品名称（用户自定义，可选）")
    private String workName;
    /**
    * 是否收藏作品：0=未收藏，1=已收藏（PRD“我的”模块收藏）
    */
    @ApiModelProperty("是否收藏作品：0=未收藏，1=已收藏（PRD“我的”模块收藏）")
    private Integer isCollected;
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
    private LocalDateTime generateTime;

}
