package com.zyy.service;

import com.zyy.dto.AigcListDTO;
import com.zyy.vo.AigcListVO;

/**
 * AIGC生成结果服务接口
 */
public interface AigcService {
    
    /**
     * 获取AIGC生成结果列表
     * @param aigcListDTO 查询参数
     * @return 分页结果
     */
    AigcListVO getAigcList(AigcListDTO aigcListDTO);
}