package com.zyy.controller.user;

import com.zyy.constant.MessageConstant;
import com.zyy.result.Result;
import com.zyy.utils.VolcengineTosUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {

    @Autowired
    private VolcengineTosUtil volcengineTosUtil;

    /**
     * 上传文件接口
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件接口")
    public Result<String> upload(MultipartFile file) {
        log.info("上传文件接口");
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;
            String url = volcengineTosUtil.upload(file.getBytes(), objectName);
            return Result.success(url);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.toString());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
