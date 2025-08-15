package com.zyy.controller.user;

import com.zyy.constant.JwtClaimsConstant;
import com.zyy.dto.UserLoginDTO;
import com.zyy.entity.User;
import com.zyy.properties.JwtProperties;
import com.zyy.result.Result;
import com.zyy.service.UserService;
import com.zyy.utils.JwtUtil;
import com.zyy.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Api(tags = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("/user/login")
    @ApiOperation("抖音用户登录")
    public Result<UserLoginVO> login(UserLoginDTO userLoginDTO) {
        log.info("抖音用户登录：{}", userLoginDTO);
        User user = userService.dyLogin(userLoginDTO);

        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .userId(user.getUserId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(userLoginVO);
    }
}
