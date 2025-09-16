package com.zyy.mapper;

import com.zyy.vo.AigcWorkVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AIGC生成结果数据访问层
 */
@Mapper
public interface AigcMapper {
    
    /**
     * 统计用户AIGC作品总数
     * @param userId 用户ID
     * @param creationType 创作类型（可选）
     * @return 总数
     */
    Long countAigcWorks(@Param("userId") Long userId, @Param("creationType") Integer creationType);
    
    /**
     * 分页查询用户AIGC作品列表
     * @param userId 用户ID
     * @param creationType 创作类型（可选）
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 作品列表
     */
    List<AigcWorkVO> selectAigcWorksByPage(
            @Param("userId") Long userId,
            @Param("creationType") Integer creationType,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );
}