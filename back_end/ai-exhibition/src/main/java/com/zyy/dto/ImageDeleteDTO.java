package com.zyy.dto;

import lombok.Data;

/**
 * 删除图片请求DTO
 */
@Data
public class ImageDeleteDTO {
    /**
     * 展览记录ID
     */
    private String recordId;
    
    /**
     * 图片ID
     */
    private String imageId;
}