package com.zyy.dto;

import lombok.Data;

/**
 * AIGC生成结果列表查询请求参数
 */
@Data
public class AigcListDTO {
    
    /**
     * 分页开始页码，默认为1
     */
    private Integer pageNum = 1;
    
    /**
     * 分页页数，默认为10
     */
    private Integer pageSize = 10;
    
    /**
     * AIGC创作类型：1=明信片图鉴，2=艺术贴纸，3=定制冰箱贴，4=魔法照片和视频
     */
    private Integer creationType;
}