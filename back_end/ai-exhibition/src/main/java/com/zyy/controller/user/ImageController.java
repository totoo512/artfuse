package com.zyy.controller.user;

import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageGenerateDTO;
import com.zyy.dto.ImageSelectDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.dto.SingleImageUploadDTO;
import com.zyy.result.Result;
import com.zyy.service.ImageService;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageGenerateVO;
import com.zyy.vo.ImageSelectVO;
import com.zyy.vo.ImageUploadVO;
import com.zyy.vo.SingleImageUploadVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 生成风格化图片接口
     * @param imageGenerateDTO
     * @return
     */
    @PostMapping("/generate")
    @ApiOperation("生成风格化图片接口")
    public Result<ImageGenerateVO> generateImages(@RequestBody ImageGenerateDTO imageGenerateDTO) {
        log.info("生成风格化图片接口: {}", imageGenerateDTO);
        
        // 参数验证
        if (imageGenerateDTO.getAnalysisId() == null) {
            return Result.error("分析结果ID不能为空");
        }
        
        ImageGenerateVO imageGenerateVO = imageService.generateImages(imageGenerateDTO);
        
        return Result.success(imageGenerateVO);
    }
    
    /**
     * 用户选择图片接口
     * @param imageSelectDTO
     * @return
     */
    @PostMapping("/select")
    @ApiOperation("用户选择图片接口")
    public Result<ImageSelectVO> selectImage(@RequestBody ImageSelectDTO imageSelectDTO) {
        log.info("用户选择图片接口: {}", imageSelectDTO);
        
        // 参数验证
        if (imageSelectDTO.getGenImageId() == null) {
            return Result.error("生成图片ID不能为空");
        }
        
        ImageSelectVO imageSelectVO = imageService.selectImage(imageSelectDTO);
        
        return Result.success(imageSelectVO);
    }
    
    /**
     * 单文件上传图片接口
     * @param singleImageUploadDTO
     * @return
     */
    @PostMapping("/single-upload")
    @ApiOperation("单文件上传图片接口")
    public Result<SingleImageUploadVO> singleUploadImage(@ModelAttribute SingleImageUploadDTO singleImageUploadDTO) {
        log.info("单文件上传图片接口: imageType={}, fileName={}", 
                singleImageUploadDTO.getImageType(), 
                singleImageUploadDTO.getImage() != null ? singleImageUploadDTO.getImage().getOriginalFilename() : "null");
        
        // 参数验证
        if (singleImageUploadDTO.getImageType() == null) {
            return Result.error("图片类型不能为空");
        }
        
        if (singleImageUploadDTO.getImageType() != 1 && singleImageUploadDTO.getImageType() != 2) {
            return Result.error("图片类型错误，只支持1=人物照，2=看展照");
        }
        
        if (singleImageUploadDTO.getImage() == null || singleImageUploadDTO.getImage().isEmpty()) {
            return Result.error("图片文件不能为空");
        }
        
        log.info("图片文件名：{}, 大小：{} bytes", 
                singleImageUploadDTO.getImage().getOriginalFilename(),
                singleImageUploadDTO.getImage().getSize());
        
        SingleImageUploadVO singleImageUploadVO = imageService.singleUploadImage(singleImageUploadDTO);
        
        return Result.success(singleImageUploadVO);
    }

}
