package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * 单文件图片上传DTO
 */
@Data
@ApiModel(description = "单文件图片上传DTO")
public class SingleImageUploadDTO {

    @ApiModelProperty(value = "图片类型：1=人物照，2=看展照", required = true)
    @NotNull(message = "图片类型不能为空")
    private Integer imageType;

    @ApiModelProperty(value = "图片文件", required = true)
    @NotNull(message = "图片文件不能为空")
    private MultipartFile image;

    @ApiModelProperty(value = "展览记录ID", required = true)
    @NotNull(message = "展览记录ID不能为空")
    private Long recordId;
}