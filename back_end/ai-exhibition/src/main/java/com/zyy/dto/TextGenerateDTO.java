package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "文本生成请求数据传输对象")
public class TextGenerateDTO {

    @ApiModelProperty("用户选择ID")
    @NotNull(message = "用户选择ID不能为空")
    private Long selectionId;

}