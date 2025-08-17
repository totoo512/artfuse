package com.zyy.controller.user;

import com.zyy.dto.VideoTaskCreateDTO;
import com.zyy.result.Result;
import com.zyy.service.VideoService;
import com.zyy.vo.VideoTaskCreateVO;
import com.zyy.vo.VideoTaskQueryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 视频生成控制器
 */
@RestController
@RequestMapping("/user/video")
@Slf4j
@Api(tags = "视频生成相关接口")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 创建视频生成任务
     * @param videoTaskCreateDTO 视频任务创建DTO
     * @return 视频任务创建结果
     */
    @PostMapping("/create-task")
    @ApiOperation("创建视频生成任务")
    public Result<VideoTaskCreateVO> createTask(@RequestBody VideoTaskCreateDTO videoTaskCreateDTO) {
        log.info("创建视频生成任务，参数：{}", videoTaskCreateDTO);
        
        VideoTaskCreateVO vo = videoService.createVideoTask(videoTaskCreateDTO);
        
        return Result.success(vo);
    }

    /**
     * 查询视频任务状态
     * @param taskId 任务ID
     * @return 视频任务状态查询结果
     */
    @GetMapping("/query-task")
    @ApiOperation("查询视频任务状态")
    public Result<VideoTaskQueryVO> queryTask(@RequestParam Long taskId) {
        log.info("查询视频任务状态，任务ID：{}", taskId);
        
        VideoTaskQueryVO vo = videoService.queryVideoTask(taskId);
        
        return Result.success(vo);
    }
}
