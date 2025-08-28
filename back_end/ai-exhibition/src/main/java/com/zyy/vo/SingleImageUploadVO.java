package com.zyy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 单文件图片上传VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "单文件图片上传VO")
public class SingleImageUploadVO {

    @ApiModelProperty(value = "图片ID")
    private Long imageId;

    @ApiModelProperty(value = "图片URL")
    private String imageUrl;
}