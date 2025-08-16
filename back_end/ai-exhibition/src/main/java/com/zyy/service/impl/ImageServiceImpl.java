package com.zyy.service.impl;

import com.zyy.constant.MessageConstant;
import com.zyy.context.BaseContext;
import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.entity.MultimodalAnalysis;
import com.zyy.entity.UploadedImage;
import com.zyy.exception.BaseException;
import com.zyy.mapper.ImageMapper;
import com.zyy.service.ImageService;
import com.zyy.utils.DouBaoSeedUtil;
import com.zyy.utils.VolcengineTosUtil;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageUploadVO;
import com.zyy.vo.ImageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    private VolcengineTosUtil volcengineTosUtil;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private DouBaoSeedUtil douBaoSeedUtil;

    @Override
    @Transactional
    public ImageUploadVO uploadImages(ImageUploadDTO imageUploadDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 上传人物照片
        String portraitImageUrl = upload(imageUploadDTO.getPortraitImage());
        UploadedImage portraitImage = new UploadedImage();
        portraitImage.setUserId(userId);
        portraitImage.setImageType(1); // 1=人物照
        portraitImage.setImageUrl(portraitImageUrl);
        portraitImage.setUploadTime(LocalDateTime.now());
        imageMapper.insert(portraitImage);
        
        // 上传展览照片列表
        List<String> exhibitionImageUrls = imageUploadDTO.getExhibitionImages().stream().map(this::upload).toList();
        List<UploadedImage> exhibitionImages = new ArrayList<>();
        for (String imageUrl : exhibitionImageUrls) {
            UploadedImage exhibitionImage = new UploadedImage();
            exhibitionImage.setUserId(userId);
            exhibitionImage.setImageType(2); // 2=看展照
            exhibitionImage.setImageUrl(imageUrl);
            exhibitionImage.setUploadTime(LocalDateTime.now());
            exhibitionImages.add(exhibitionImage);
        }
        imageMapper.insertBatch(exhibitionImages);
        
        // 构建返回对象
        ImageUploadVO result = new ImageUploadVO();
        
        // 设置人物照片信息
        result.setPortraitImageId(portraitImage.getImageId());
        ImageVO portraitImageVO = new ImageVO();
        portraitImageVO.setImageId(portraitImage.getImageId());
        portraitImageVO.setImageUrl(portraitImage.getImageUrl());
        result.setPortraitImage(portraitImageVO);
        
        // 设置展览照片信息
        List<Long> exhibitionImageIds = new ArrayList<>();
        List<ImageVO> exhibitionImageVOs = new ArrayList<>();
        for (UploadedImage exhibitionImage : exhibitionImages) {
            exhibitionImageIds.add(exhibitionImage.getImageId());
            
            ImageVO exhibitionImageVO = new ImageVO();
            exhibitionImageVO.setImageId(exhibitionImage.getImageId());
            exhibitionImageVO.setImageUrl(exhibitionImage.getImageUrl());
            exhibitionImageVOs.add(exhibitionImageVO);
        }
        result.setExhibitionImageIds(exhibitionImageIds);
        result.setExhibitionImages(exhibitionImageVOs);
        
        return result;
    }

    @Override
    @Transactional
    public ImageAnalysisVO analyzeImages(ImageAnalysisDTO imageAnalysisDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 验证图片是否存在
        UploadedImage portraitImage = imageMapper.getById(imageAnalysisDTO.getPortraitImageId());
        if (portraitImage == null) {
            throw new BaseException("人物照片不存在");
        }
        
        List<UploadedImage> exhibitionImages = new ArrayList<>();
        for (Long exhibitionImageId : imageAnalysisDTO.getExhibitionImageIds()) {
            UploadedImage exhibitionImage = imageMapper.getById(exhibitionImageId);
            if (exhibitionImage == null) {
                throw new BaseException("展览照片不存在: " + exhibitionImageId);
            }
            exhibitionImages.add(exhibitionImage);
        }
        
        // 调用豆包AI模型进行分析
        String analysisResult;
        try {
            // 构建分析提示词
            String prompt = "";
            
            // 收集所有图片URL
            List<String> imageUrls = new ArrayList<>();
            imageUrls.add(portraitImage.getImageUrl());
            for (UploadedImage exhibitionImage : exhibitionImages) {
                imageUrls.add(exhibitionImage.getImageUrl());
            }
            
            // 调用豆包AI进行图文分析
            analysisResult = douBaoSeedUtil.chatWithImages(prompt, imageUrls);
            log.info("AI分析完成，用户ID: {}, 分析结果长度: {}", userId, analysisResult.length());
        } catch (Exception e) {
            log.error("AI分析失败，用户ID: {}, 错误: {}", userId, e.getMessage(), e);
            analysisResult = "AI分析暂时不可用，请稍后重试。错误信息: " + e.getMessage();
        }
        
        // 保存分析结果
        MultimodalAnalysis analysis = new MultimodalAnalysis();
        analysis.setUserId(userId);
        analysis.setPortraitImageId(imageAnalysisDTO.getPortraitImageId());
        analysis.setExhibitionImageIds(String.join(",", imageAnalysisDTO.getExhibitionImageIds().stream().map(String::valueOf).toArray(String[]::new)));
        analysis.setAnalysisResult(analysisResult);
        analysis.setAnalysisTime(LocalDateTime.now());
        
        imageMapper.insertAnalysis(analysis);
        
        return ImageAnalysisVO.builder()
                .analysisId(analysis.getAnalysisId())
                .analysisResult(analysisResult)
                .build();
    }

    private String upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID() + extension;
            return volcengineTosUtil.upload(file.getBytes(), objectName);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.toString());
            throw new BaseException(MessageConstant.UPLOAD_FAILED);
        }
    }
}
