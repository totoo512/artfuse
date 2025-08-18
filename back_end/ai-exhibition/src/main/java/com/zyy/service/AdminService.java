package com.zyy.service;

import com.zyy.dto.AdminLoginDTO;
import com.zyy.vo.AdminLoginVO;

/**
 * 管理端服务接口
 */
public interface AdminService {

    /**
     * 管理端登录
     * @param adminLoginDTO 登录参数
     * @return 登录结果
     */
    AdminLoginVO login(AdminLoginDTO adminLoginDTO);

}