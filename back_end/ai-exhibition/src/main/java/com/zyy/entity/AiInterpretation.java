package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* AI看展模块 - 结构化解读结果表（PRD核心AI能力数据存储）
* @TableName ai_interpretation
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiInterpretation implements Serializable {

    /**
    * 解读结果唯一标识
    */
    @ApiModelProperty("解读结果唯一标识")
    private Long interpretationId;
    /**
    * 关联观展会话（exhibition_session.session_id）
    */
    @ApiModelProperty("关联观展会话（exhibition_session.session_id）")
    private Long sessionId;
    /**
    * 关联上传图片（uploaded_image.image_id）
    */
    @ApiModelProperty("关联上传图片（uploaded_image.image_id）")
    private Long mediaId;
    /**
    * 作品简介（PRD结构化解读字段1）
    */
    @ApiModelProperty("作品简介（PRD结构化解读字段1）")
    private String analysisIntroduction;
    /**
    * 艺术风格（PRD结构化解读字段2）
    */
    @ApiModelProperty("艺术风格（PRD结构化解读字段2）")
    private String analysisStyle;
    /**
    * 象征意义（PRD结构化解读字段3）
    */
    @ApiModelProperty("象征意义（PRD结构化解读字段3）")
    private String analysisSymbolism;
    /**
    * 解读生成时间
    */
    @ApiModelProperty("解读生成时间")
    private LocalDateTime createTime;

}
