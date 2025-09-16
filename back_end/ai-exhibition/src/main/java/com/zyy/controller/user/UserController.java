package com.zyy.controller.user;

import com.zyy.context.BaseContext;
import com.zyy.dto.UserLoginDTO;
import com.zyy.dto.UserRegisterDTO;
import com.zyy.dto.UserUpdateDTO;
import com.zyy.entity.User;
import com.zyy.result.Result;
import com.zyy.service.UserService;
import com.zyy.vo.UserInfoVO;
import com.zyy.vo.UserLoginVO;
import com.zyy.vo.UserRegisterVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     * @param userRegisterDTO 注册信息
     * @return 注册结果
     */
//    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<UserRegisterVO> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        UserRegisterVO userRegisterVO = userService.register(userRegisterDTO);
        return Result.success(userRegisterVO);
    }

    /**
     * 抖音用户登录
     * @param userLoginDTO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation("抖音用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("抖音用户登录：{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.dyLogin(userLoginDTO);
        return Result.success(userLoginVO);
    }

    /**
     * 修改用户信息
     * @param userUpdateDTO 用户更新信息
     * @return 更新结果
     */
    @PutMapping("/update")
    @ApiOperation("修改用户信息")
    public Result<Object> update(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        log.info("修改用户信息：{}", userUpdateDTO);
        userService.update(userUpdateDTO);
        return Result.success();
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<UserInfoVO> getUserInfo() {
        // 从JWT token中获取用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("获取用户信息，用户ID：{}", userId);
        
        UserInfoVO userInfoVO = userService.getUserInfo(userId);
        return Result.success(userInfoVO);
    }
}