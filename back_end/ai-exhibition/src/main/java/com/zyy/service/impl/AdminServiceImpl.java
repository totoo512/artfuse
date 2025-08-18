package com.zyy.service.impl;

import com.zyy.constant.JwtClaimsConstant;
import com.zyy.dto.AdminLoginDTO;
import com.zyy.entity.User;
import com.zyy.exception.LoginFailedException;
import com.zyy.mapper.UserMapper;
import com.zyy.properties.JwtProperties;
import com.zyy.service.AdminService;
import com.zyy.utils.JwtUtil;
import com.zyy.vo.AdminLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

}