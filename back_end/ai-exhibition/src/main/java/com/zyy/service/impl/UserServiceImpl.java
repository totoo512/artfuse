package com.zyy.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyy.constant.MessageConstant;
import com.zyy.dto.UserLoginDTO;
import com.zyy.entity.User;
import com.zyy.exception.BaseException;
import com.zyy.exception.LoginFailedException;
import com.zyy.mapper.UserMapper;
import com.zyy.properties.DouYinProperties;
import com.zyy.service.UserService;
import com.zyy.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final String DY_LOGIN_URL = "https://developer.toutiao.com/api/apps/v2/jscode2session";

    @Autowired
    private DouYinProperties douYinProperties;
    @Autowired
    private UserMapper userMapper;

    /**
     * 抖音用户登录
     * @param userLoginDTO
     * @return
     */
    public User dyLogin(UserLoginDTO userLoginDTO) {
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
                    .build();
            userMapper.insert(user);
        }

        return user;
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
