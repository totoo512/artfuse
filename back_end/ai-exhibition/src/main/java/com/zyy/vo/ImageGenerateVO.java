package com.zyy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageGenerateVO implements Serializable {
    
    /**
     * 生成的图片ID列表
     */
    private List<Long> generatedImageIds;
    
    /**
     * 生成的图片信息列表
     */
    private List<ImageVO> images;
}