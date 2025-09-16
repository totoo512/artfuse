package com.zyy.controller.user;

import com.zyy.dto.AigcListDTO;
import com.zyy.result.Result;
import com.zyy.service.AigcService;
import com.zyy.vo.AigcListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AIGC生成结果控制器
 */
@RestController
@RequestMapping("/user/aigc")
@Slf4j
public class AigcController {
    
    @Autowired
    private AigcService aigcService;
    
    /**
     * 获取全部生成结果列表
     * @param aigcListDTO 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<AigcListVO> getAigcList(AigcListDTO aigcListDTO) {
        log.info("获取AIGC生成结果列表，参数: {}", aigcListDTO);
        
        // 参数校验
        if (aigcListDTO.getPageNum() == null || aigcListDTO.getPageNum() < 1) {
            aigcListDTO.setPageNum(1);
        }
        if (aigcListDTO.getPageSize() == null || aigcListDTO.getPageSize() < 1) {
            aigcListDTO.setPageSize(10);
        }
        
        AigcListVO result = aigcService.getAigcList(aigcListDTO);
        
        log.info("获取AIGC生成结果列表成功，总数: {}", result.getTotal());
        return Result.success(result);
    }
}