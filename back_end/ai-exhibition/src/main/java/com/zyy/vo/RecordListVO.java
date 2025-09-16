package com.zyy.vo;

import lombok.Data;

/**
 * 看展记录列表VO
 */
@Data
public class RecordListVO {
    /**
     * 记录ID
     */
    private String recordId;
    
    /**
     * 封面图片URL
     */
    private String coverImageUrl;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 画廊名称
     */
    private String galleryName;
    
    /**
     * 展览日期
     */
    private String exhibitionDate;
}