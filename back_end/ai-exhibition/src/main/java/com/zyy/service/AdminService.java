package com.zyy.service;

import com.zyy.dto.AdminLoginDTO;
import com.zyy.vo.AdminLoginVO;
import com.zyy.vo.VideoTaskVO;

import java.util.List;

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
    
    /**
     * 查询所有未完成的视频任务
     * @return 未完成的视频任务列表
     */
    List<VideoTaskVO> queryUnfinishedVideoTasks();
}