package com.zyy.dto;

import lombok.Data;

import java.util.List;

/**
 * 生成明信片图鉴请求DTO
 */
@Data
public class GeneratePostcardDTO {
    
    /**
     * 风格ID
     */
    private Integer styleId;
    
    /**
     * 图片ID列表
     */
    private List<Long> imageIds;
}