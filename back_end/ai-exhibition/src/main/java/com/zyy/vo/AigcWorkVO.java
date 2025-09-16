package com.zyy.vo;

import lombok.Data;

/**
 * AIGC作品信息响应类
 */
@Data
public class AigcWorkVO {
    
    /**
     * 作品ID
     */
    private String workId;
    
    /**
     * 封面图片URL
     */
    private String url;
    
    /**
     * 风格名称
     */
    private String styleName;
}