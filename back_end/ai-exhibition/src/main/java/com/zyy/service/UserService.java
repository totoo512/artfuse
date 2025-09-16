package com.zyy.service;

import com.zyy.dto.UserLoginDTO;
import com.zyy.dto.UserRegisterDTO;
import com.zyy.dto.UserUpdateDTO;
import com.zyy.vo.UserInfoVO;
import com.zyy.vo.UserLoginVO;
import com.zyy.vo.UserRegisterVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     * @param userRegisterDTO 注册信息
     * @return 注册结果
     */
    UserRegisterVO register(UserRegisterDTO userRegisterDTO);

    /**
     * 抖音用户登录
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    UserLoginVO dyLogin(UserLoginDTO userLoginDTO);

    /**
     * 更新用户信息
     * @param userUpdateDTO 用户更新信息
     */
    void update(UserUpdateDTO userUpdateDTO);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);
}