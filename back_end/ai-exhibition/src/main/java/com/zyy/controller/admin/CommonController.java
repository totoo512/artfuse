package com.zyy.controller.admin;

import com.zyy.dto.FileUploadDTO;
import com.zyy.result.Result;
import com.zyy.service.VideoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "管理端通用接口")
@Slf4j
public class CommonController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public Result<String> upload(@ModelAttribute FileUploadDTO fileUploadDTO) {
        log.info("文件上传：{}", fileUploadDTO.getFile().getOriginalFilename());
        try {
            String url = videoService.updateVideoTaskUrl(fileUploadDTO.getTaskId(), fileUploadDTO.getFile());
            return Result.success(url);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败");
        }
    }
}
