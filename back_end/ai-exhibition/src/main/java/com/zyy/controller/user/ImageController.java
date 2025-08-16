package com.zyy.controller.user;

import com.zyy.constant.MessageConstant;
import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.result.Result;
import com.zyy.service.ImageService;
import com.zyy.utils.VolcengineTosUtil;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageUploadVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user/image")
@Slf4j
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传文件接口
     * @param imageUploadDTO
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件接口")
    public Result<ImageUploadVO> uploadImages(@ModelAttribute ImageUploadDTO imageUploadDTO) {
        log.info("上传文件接口: {}", imageUploadDTO);
        
        // 参数验证
        if (imageUploadDTO.getPortraitImage() == null || imageUploadDTO.getPortraitImage().isEmpty()) {
            log.warn("头像图片为空");
            return Result.error("头像图片不能为空");
        }
        
        if (imageUploadDTO.getExhibitionImages() == null || imageUploadDTO.getExhibitionImages().isEmpty()) {
            log.warn("展览图片为空");
            return Result.error("展览图片不能为空");
        }
        
        log.info("头像图片文件名：{}, 大小：{} bytes", 
                imageUploadDTO.getPortraitImage().getOriginalFilename(),
                imageUploadDTO.getPortraitImage().getSize());
        
        log.info("展览图片数量：{}", imageUploadDTO.getExhibitionImages().size());
        for (int i = 0; i < imageUploadDTO.getExhibitionImages().size(); i++) {
            log.info("展览图片{}文件名：{}, 大小：{} bytes", 
                    i + 1,
                    imageUploadDTO.getExhibitionImages().get(i).getOriginalFilename(),
                    imageUploadDTO.getExhibitionImages().get(i).getSize());
        }
        
        ImageUploadVO imageUploadVO = imageService.uploadImages(imageUploadDTO);
        return Result.success(imageUploadVO);
    }
    
    /**
     * 图片分析接口
     * @param imageAnalysisDTO
     * @return
     */
    @PostMapping("/analysis")
    @ApiOperation("图片分析接口")
    public Result<ImageAnalysisVO> analyzeImages(@RequestBody ImageAnalysisDTO imageAnalysisDTO) {
        log.info("图片分析接口: {}", imageAnalysisDTO);
        
        // 参数验证
        if (imageAnalysisDTO.getPortraitImageId() == null) {
            return Result.error("人物照片ID不能为空");
        }
        
        if (imageAnalysisDTO.getExhibitionImageIds() == null || imageAnalysisDTO.getExhibitionImageIds().isEmpty()) {
            return Result.error("展览照片ID列表不能为空");
        }
        
        ImageAnalysisVO imageAnalysisVO = imageService.analyzeImages(imageAnalysisDTO);
        
        return Result.success(imageAnalysisVO);
    }
}
