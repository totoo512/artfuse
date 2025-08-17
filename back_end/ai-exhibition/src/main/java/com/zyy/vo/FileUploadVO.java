package com.zyy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件上传VO
 */
@Data
@ApiModel(description = "文件上传VO")
public class FileUploadVO {

    /**
     * 文件URL
     */
    @ApiModelProperty(value = "文件URL")
    private String fileUrl;
}