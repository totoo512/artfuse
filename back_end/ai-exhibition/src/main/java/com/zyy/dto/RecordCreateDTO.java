package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 新建看展记录DTO
 */
@Data
@ApiModel(description = "新建看展记录DTO")
public class RecordCreateDTO {

    @ApiModelProperty(value = "展览名称", required = true)
    @NotBlank(message = "展览名称不能为空")
    private String title;

    @ApiModelProperty(value = "美术馆名称", required = true)
    @NotBlank(message = "美术馆名称不能为空")
    private String galleryName;

    @ApiModelProperty(value = "看展日期，格式YYYY-MM-DD，默认当前日期")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为YYYY-MM-DD")
    private String exhibitionDate;

    @ApiModelProperty(value = "国家", required = true)
    @NotBlank(message = "国家不能为空")
    private String country;

    @ApiModelProperty(value = "城市", required = true)
    @NotBlank(message = "城市不能为空")
    private String city;

    @ApiModelProperty(value = "艺术家")
    private String artist;

    @ApiModelProperty(value = "风格标签，逗号分割")
    private String styleTags;

    @ApiModelProperty(value = "观展感想")
    private String notes;

    @ApiModelProperty(value = "AI总结")
    private String aiSummary;
}