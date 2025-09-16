package com.zyy.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* AIGC风格表
* @TableName aigc_style
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AigcStyle implements Serializable {

    /**
    * 风格id
    */
    @ApiModelProperty("风格id")
    private Integer styleId;
    /**
    * 风格名称
    */
    @ApiModelProperty("风格名称")
    private String styleName;
    /**
    * 创作类型：1=明信片图鉴，2=艺术贴纸，3=定制冰箱贴，4=魔法照片和视频
    */
    @ApiModelProperty("创作类型：1=明信片图鉴，2=艺术贴纸，3=定制冰箱贴，4=魔法照片和视频")
    private Integer creationType;
    /**
    * 提示词
    */
    @ApiModelProperty("提示词")
    private String prompt;

}
