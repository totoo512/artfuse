package com.zyy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "文本生成响应数据传输对象")
public class TextGenerateVO {

    @ApiModelProperty("文本结果唯一标识")
    private Long textId;

    @ApiModelProperty("朋友圈文案")
    private String momentsCopy;

}