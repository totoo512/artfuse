package com.zyy.service.impl;

import com.zyy.context.BaseContext;
import com.zyy.dto.AigcListDTO;
import com.zyy.mapper.AigcMapper;
import com.zyy.service.AigcService;
import com.zyy.vo.AigcListVO;
import com.zyy.vo.AigcWorkVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AIGC生成结果服务实现类
 */
@Service
@Slf4j
public class AigcServiceImpl implements AigcService {
    
    @Autowired
    private AigcMapper aigcMapper;
    
    @Override
    public AigcListVO getAigcList(AigcListDTO aigcListDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("获取AIGC生成结果列表，用户ID: {}, 参数: {}", userId, aigcListDTO);
        
        // 计算分页偏移量
        int offset = (aigcListDTO.getPageNum() - 1) * aigcListDTO.getPageSize();
        
        // 查询总数
        Long total = aigcMapper.countAigcWorks(userId, aigcListDTO.getCreationType());
        
        // 查询分页数据
        List<AigcWorkVO> records = aigcMapper.selectAigcWorksByPage(
                userId, 
                aigcListDTO.getCreationType(), 
                offset, 
                aigcListDTO.getPageSize()
        );
        
        // 构建返回结果
        AigcListVO result = new AigcListVO();
        result.setTotal(total);
        result.setRecords(records);
        
        log.info("获取AIGC生成结果列表成功，用户ID: {}, 总数: {}, 当前页记录数: {}", 
                userId, total, records.size());
        
        return result;
    }
}