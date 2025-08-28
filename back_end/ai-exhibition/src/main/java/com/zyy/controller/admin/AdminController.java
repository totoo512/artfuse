package com.zyy.controller.admin;

import com.zyy.dto.AdminLoginDTO;
import com.zyy.result.Result;
import com.zyy.service.AdminService;
import com.zyy.vo.AdminLoginVO;
import com.zyy.vo.VideoTaskVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

import java.util.List;

/**
 * 管理端控制器
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "管理端相关接口")
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 管理端登录
     * @param adminLoginDTO 登录参数
     * @return 登录结果
     */
    @PostMapping("/login")
    @ApiOperation("管理端登录")
    public Result<AdminLoginVO> login(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理端登录，参数: {}", adminLoginDTO);
        
        AdminLoginVO adminLoginVO = adminService.login(adminLoginDTO);
        
        return Result.success(adminLoginVO);
    }
    
    /**
     * 获取所有未完成的视频任务
     * @return 未完成的视频任务列表
     */
    @GetMapping("/video/query-unfinished-tasks")
    @ApiOperation("获取所有未完成的视频任务")
    public Result<List<VideoTaskVO>> queryUnfinishedVideoTasks() {
        log.info("获取所有未完成的视频任务");
        
        List<VideoTaskVO> videoTasks = adminService.queryUnfinishedVideoTasks();
        
        return Result.success(videoTasks);
    }
}