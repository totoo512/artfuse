package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 编辑看展记录DTO
 */
@Data
@ApiModel(description = "编辑看展记录DTO")
public class RecordUpdateDTO {

    @ApiModelProperty(value = "看展记录ID", required = true)
    @NotNull(message = "看展记录ID不能为空")
    private Long recordId;

    @ApiModelProperty(value = "展览名称")
    private String title;

    @ApiModelProperty(value = "美术馆名称")
    private String galleryName;

    @ApiModelProperty(value = "看展日期，格式YYYY-MM-DD")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式必须为YYYY-MM-DD")
    private String exhibitionDate;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "艺术家")
    private String artist;

    @ApiModelProperty(value = "风格标签，逗号分割")
    private String styleTags;

    @ApiModelProperty(value = "观展感想")
    private String notes;

    @ApiModelProperty(value = "AI总结")
    private String aiSummary;

    @ApiModelProperty(value = "封面图片URL")
    private String coverImageUrl;
}