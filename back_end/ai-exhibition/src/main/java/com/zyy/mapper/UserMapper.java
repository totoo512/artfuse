package com.zyy.mapper;

import com.zyy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     * @param user 用户信息
     */
    void insert(User user);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUsername(@Param("username") String username);

    /**
     * 根据用户ID查询用户
     * @param userId 用户ID
     * @return 用户信息
     */
    User getById(@Param("userId") Long userId);

    /**
     * 根据openid查询用户
     * @param openid 微信openid
     * @return 用户信息
     */
    User getByOpenid(@Param("openid") String openid);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    void update(User user);
}