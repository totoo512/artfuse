package com.zyy.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalysisVO implements Serializable {
    
    /**
     * 分析结果ID
     */
    private Long analysisId;
    
    /**
     * 分析结果内容
     */
    private String analysisResult;
}