package com.zyy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyy.constant.JwtClaimsConstant;
import com.zyy.constant.MessageConstant;
import com.zyy.dto.UserLoginDTO;
import com.zyy.dto.UserRegisterDTO;
import com.zyy.dto.UserUpdateDTO;
import com.zyy.entity.User;
import com.zyy.exception.AccountExistException;
import com.zyy.exception.BaseException;
import com.zyy.exception.LoginFailedException;
import com.zyy.mapper.UserMapper;
import com.zyy.properties.DouYinProperties;
import com.zyy.properties.JwtProperties;
import com.zyy.service.UserService;
import com.zyy.utils.HttpClientUtil;
import com.zyy.utils.JwtUtil;
import com.zyy.vo.UserInfoVO;
import com.zyy.vo.UserLoginVO;
import com.zyy.vo.UserRegisterVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String DY_LOGIN_URL = "https://developer.toutiao.com/api/apps/v2/jscode2session";

    @Autowired
    private DouYinProperties douYinProperties;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     * @param userRegisterDTO 注册信息
     * @return 注册结果
     */
    @Override
    public UserRegisterVO register(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String password = userRegisterDTO.getPassword();

        // 检查用户名是否已存在
        User existUser = userMapper.getByUsername(username);
        if (existUser != null) {
            throw new AccountExistException(MessageConstant.ACCOUNT_EXIST);
        }

        // 创建新用户
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .generatedWorksCount(0)
                .points(0)
                .membershipStatus(0)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 插入用户
        userMapper.insert(user);

        // 生成JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );

        // 构建返回结果
        return UserRegisterVO.builder()
                .userId(user.getUserId())
                .token(token)
                .build();
    }

    /**
     * 抖音用户登录
     *
     * @param userLoginDTO
     * @return
     */
    public UserLoginVO dyLogin(UserLoginDTO userLoginDTO) {
        if (userLoginDTO.getCode() == null) {
            throw new BaseException(MessageConstant.LOGIN_FAILED);
        }

        // 调用抖音接口，获取openid
        String openid = getOpenid(userLoginDTO);

        if (openid == null || openid.isEmpty()) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 根据openid查询用户信息
        User user = userMapper.getByOpenid(openid);

        // 若不存在用户，则自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return userLoginVO;
    }

    /**
     * 更新用户信息
     * @param userUpdateDTO 用户更新信息
     */
    @Override
    public void update(UserUpdateDTO userUpdateDTO) {
        log.info("更新用户信息，用户ID: {}", userUpdateDTO.getUserId());

        // 构建用户实体
        User user = User.builder()
                .userId(userUpdateDTO.getUserId())
                .updateTime(LocalDateTime.now())
                .build();

        // 如果密码不为空，进行BCrypt加密
        if (StringUtils.hasText(userUpdateDTO.getPassword())) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        // 更新用户信息
        userMapper.update(user);
    }

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        log.info("获取用户信息，用户ID: {}", userId);

        // 根据用户ID查询用户信息
        User user = userMapper.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 构建返回结果
        return UserInfoVO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .avatarUrl(user.getAvatarUrl())
                .phone(user.getPhone())
                .generatedWorksCount(user.getGeneratedWorksCount())
                .points(user.getPoints())
                .membershipStatus(user.getMembershipStatus())
                .build();
    }

    private String getOpenid(UserLoginDTO userLoginDTO) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", douYinProperties.getAppid());
        map.put("secret", douYinProperties.getSecret());
        map.put("code", userLoginDTO.getCode());
        String json;
        try {
            json = HttpClientUtil.doPost4Json(DY_LOGIN_URL, map);
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getJSONObject("data").getString("openid");
    }
}