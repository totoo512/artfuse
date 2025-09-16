package com.zyy.dto;

import lombok.Data;

/**
 * 图片收藏/取消收藏请求DTO
 */
@Data
public class ImageFavoriteDTO {
    /**
     * 图片ID
     */
    private String imageId;
    
    /**
     * 是否收藏 0=未收藏，1=已收藏
     */
    private Integer isFavorite;
}