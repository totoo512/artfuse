package com.zyy.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class FileUploadDTO {
    @NotNull(message = "任务ID不能为空")
    private Long taskId;
    
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
}