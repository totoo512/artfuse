package com.zyy.vo;

import lombok.Data;
import java.util.List;

/**
 * 看展记录详情VO
 */
@Data
public class RecordDetailVO {
    
    /**
     * 记录信息
     */
    private RecordInfo recordInfo;
    
    /**
     * 图片列表
     */
    private List<ImageDetailVO> imageList;
    
    /**
     * 记录信息内部类
     */
    @Data
    public static class RecordInfo {
        
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
}