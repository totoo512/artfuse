package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;
import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 多模态分析结果表
* @TableName multimodal_analysis
*/
@Data
public class MultimodalAnalysis implements Serializable {

    /**
    * 分析结果唯一标识
    */
    @ApiModelProperty("分析结果唯一标识")
    private Long analysisId;
    /**
    * 关联用户
    */
    @ApiModelProperty("关联用户")
    private Long userId;
    /**
    * 关联人物照
    */
    @ApiModelProperty("关联人物照")
    private Long portraitImageId;
    /**
    * 关联看展照ID（逗号分隔）
    */
    @ApiModelProperty("关联看展照ID（逗号分隔）")
    private String exhibitionImageIds;
    /**
    * 人物特征描述
    */
    @ApiModelProperty("人物特征和作品特征融合输出生图提示词")
    private String analysisResult;
    /**
    * 分析完成时间
    */
    @ApiModelProperty("分析完成时间")
    private LocalDateTime analysisTime;

}
