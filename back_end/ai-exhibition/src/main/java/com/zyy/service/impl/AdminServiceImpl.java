package com.zyy.service.impl;

import com.zyy.constant.JwtClaimsConstant;
import com.zyy.dto.AdminLoginDTO;
import com.zyy.entity.User;
import java.util.Map;
import com.zyy.exception.LoginFailedException;
import com.zyy.mapper.ImageMapper;
import com.zyy.mapper.UserMapper;
import com.zyy.mapper.VideoMapper;
import com.zyy.properties.JwtProperties;
import com.zyy.service.AdminService;
import com.zyy.utils.JwtUtil;
import com.zyy.vo.AdminLoginVO;
import com.zyy.vo.VideoTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理端服务实现类
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;
    
    @Autowired
    private VideoMapper videoMapper;
    
    @Autowired
    private ImageMapper imageMapper;

    /**
     * 管理端登录
     * @param adminLoginDTO 登录参数
     * @return 登录结果
     */
    @Override
    public AdminLoginVO login(AdminLoginDTO adminLoginDTO) {
        log.info("管理端登录，code: {}", adminLoginDTO.getCode());
        
        // 根据code查询用户（这里假设code就是openid）
        User user = userMapper.getByOpenid(adminLoginDTO.getCode());
        
        // 校验用户是否存在
        if (user == null) {
            log.error("管理端登录失败，用户不存在，code: {}", adminLoginDTO.getCode());
            throw new LoginFailedException("用户不存在");
        }
        
        log.info("管理端登录成功，用户ID: {}", user.getUserId());
        
        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );
        
        // 构建返回结果

        return AdminLoginVO.builder()
                .token(token)
                .build();
    }

    /**
     * 查询所有未完成的视频任务
     * @return 未完成的视频任务列表
     */
    @Override
    public List<VideoTaskVO> queryUnfinishedVideoTasks() {
        log.info("查询所有未完成的视频任务");
        
        // 使用JOIN查询直接获取视频任务及其关联的图片URL
        List<Map<String, Object>> taskMaps = videoMapper.selectUnfinishedVideoTasksWithImages();
        List<VideoTaskVO> result = new ArrayList<>();
        
        // 转换为VO对象
        for (Map<String, Object> taskMap : taskMaps) {
            VideoTaskVO vo = new VideoTaskVO();
            
            // 设置基本属性
            vo.setTaskId(((Number) taskMap.get("task_id")).longValue());
            vo.setUserId(((Number) taskMap.get("user_id")).longValue());
            vo.setSelectionId(((Number) taskMap.get("selection_id")).longValue());
            vo.setRefImageId(((Number) taskMap.get("ref_image_id")).longValue());
            vo.setStatus((Integer) taskMap.get("status"));
            vo.setCreateTime((java.time.LocalDateTime) taskMap.get("create_time"));
            vo.setUpdateTime((java.time.LocalDateTime) taskMap.get("update_time"));
            
            // 设置图片URL
            vo.setImageUrl1((String) taskMap.get("gen_image_url"));
            vo.setImageUrl2((String) taskMap.get("ref_image_url"));
            
            // 设置视频生成提示词
            vo.setPrompt((String) taskMap.get("video_prompt"));
            
            // 处理可能为null的情况
            if (vo.getImageUrl1() == null) {
                vo.setImageUrl1("");
            }
            if (vo.getImageUrl2() == null) {
                vo.setImageUrl2("");
            }
            if (vo.getPrompt() == null) {
                vo.setPrompt("");
            }
            
            result.add(vo);
        }
        
        return result;
    }
}