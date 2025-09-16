package com.zyy.vo;

import lombok.Data;

/**
 * 图片详情VO
 */
@Data
public class ImageDetailVO {
    
    /**
     * 图片ID
     */
    private String imageId;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 是否收藏（1-收藏，0-未收藏）
     */
    private String isFavorite;
    
    /**
     * 个人备注
     */
    private String personalNote;
    
    /**
     * 上传时间
     */
    private String uploadTime;
}