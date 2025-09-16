package com.zyy.vo;

import lombok.Data;
import java.util.List;

/**
 * AIGC生成结果列表分页响应类
 */
@Data
public class AigcListVO {
    
    /**
     * 记录总数
     */
    private Long total;
    
    /**
     * 记录列表
     */
    private List<AigcWorkVO> records;
}