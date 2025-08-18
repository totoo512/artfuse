package com.zyy.service.impl;

import com.zyy.constant.MessageConstant;
import com.zyy.context.BaseContext;
import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageGenerateDTO;
import com.zyy.dto.ImageSelectDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.dto.SingleImageUploadDTO;
import com.zyy.entity.GeneratedImage;
import com.zyy.entity.MultimodalAnalysis;
import com.zyy.entity.UploadedImage;
import com.zyy.entity.UserSelection;
import com.zyy.exception.BaseException;
import com.zyy.mapper.ImageMapper;
import com.zyy.service.ImageService;
import com.zyy.utils.DouBaoSeedUtil;
import com.zyy.utils.KelingAiUtil;
import com.zyy.utils.VolcengineTosUtil;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageGenerateVO;
import com.zyy.vo.ImageSelectVO;
import com.zyy.vo.ImageUploadVO;
import com.zyy.vo.ImageVO;
import com.zyy.vo.SingleImageUploadVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private KelingAiUtil kelingAiUtil;

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
            String prompt = """
                    # ROLE
                    你是一位顶级的AI绘画金牌提示词工程师（Prompt Engineer）。你擅长分析人物照片和艺术作品，然后将两者的精髓融合成一段富有创造力、指令清晰、细节丰富的提示词，以指导AI绘画模型生成惊艳的作品。
                    # TASK
                    你的任务是同时分析下方提供的两张图片：[INPUT_IMAGE_1_PERSON] 和 [INPUT_IMAGE_2_ARTWORK]。然后，直接生成一段约300字的、高质量的中文自然语言提示词（Prompt）。这段文字将作为下一阶段输入给文生图模型的最终指令。
                    # INPUTS
                    INPUT_IMAGE_1_PERSON: [此处将传入用户上传的人物照片]
                    INPUT_IMAGE_2_ARTWORK: [此处将传入用户选择的穆夏风格画作]
                    # PROMPT COMPOSITION GUIDELINES (你生成Prompt时必须遵循的规则):
                    核心主体: 提示词的开篇必须明确主体是基于INPUT_IMAGE_1_PERSON的人物。要清晰描述其性别、面部特征和神态，并强调这些是必须被精确还原的核心要素。
                    关键特征保留: 必须单独、明确地指令要完全保留人物的发型与发色。这是不可协商的指令，要用强烈的语气表达。
                    艺术风格应用: 明确指出画作的艺术风格是阿尔丰斯·穆夏（Alphonse Mucha）的新艺术运动（Art Nouveau）风格。需要从INPUT_IMAGE_2_ARTWORK中提取关键视觉元素并融入提示词，例如流畅蜿蜒的“鞭绳纹”线条、华丽的装饰性圆形背景、以及标志性的花卉与植物图案。
                    服装与色彩: 指令AI将人物的现代服装重新设计为符合穆夏风格的、充满垂坠感和优雅褶皱的古典长袍。色彩方面，要求采用INPUT_IMAGE_2_ARTWORK中温暖、复古的色调，如金色、奶油色、赭石色等。
                    画面要求: 在提示词的末尾，加入提升画面质量的通用词汇，例如杰作、超高清细节、8K、精致华丽、光影细腻等。
                    # CONSTRAINTS
                    你的最终输出只能是那段生成的中文提示词，一个完整的文本块。
                    不要包含任何解释、标题、JSON代码或Markdown格式。
                    语言风格要直接、自信，就像在给一个AI艺术家下达明确的创作指令。
                    将总字数控制在300字左右。""";
            
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

    @Override
    @Transactional
    public ImageGenerateVO generateImages(ImageGenerateDTO imageGenerateDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 验证分析结果是否存在
        MultimodalAnalysis analysis = imageMapper.getAnalysisById(imageGenerateDTO.getAnalysisId());
        if (analysis == null) {
            throw new BaseException("分析结果不存在");
        }
        
        // 验证分析结果是否属于当前用户
        if (!analysis.getUserId().equals(userId)) {
            throw new BaseException("无权限访问该分析结果");
        }
        
        // 获取人物图片和展览图片URL
        UploadedImage portraitImage = imageMapper.getById(analysis.getPortraitImageId());
        if (portraitImage == null) {
            throw new BaseException("人物照片不存在");
        }
        
        String[] exhibitionImageIdArray = analysis.getExhibitionImageIds().split(",");
        List<String> exhibitionImageUrls = new ArrayList<>();
        for (String exhibitionImageIdStr : exhibitionImageIdArray) {
            Long exhibitionImageId = Long.parseLong(exhibitionImageIdStr.trim());
            UploadedImage exhibitionImage = imageMapper.getById(exhibitionImageId);
            if (exhibitionImage != null) {
                exhibitionImageUrls.add(exhibitionImage.getImageUrl());
            }
        }
        
        // 调用可灵AI生成风格化图片
        List<String> generatedImageUrls;
        try {
            // 构建提示词，基于分析结果
            String prompt = analysis.getAnalysisResult();
            
            // 调用可灵API生成图片
            generatedImageUrls = kelingAiUtil.multiImage2Image(
                portraitImage.getImageUrl(), 
                exhibitionImageUrls, 
                prompt
            );
            
            log.info("可灵AI生成图片成功，用户ID: {}, 分析ID: {}, 生成图片数量: {}", 
                    userId, imageGenerateDTO.getAnalysisId(), generatedImageUrls.size());
        } catch (Exception e) {
            log.error("可灵AI生成图片失败，用户ID: {}, 分析ID: {}, 错误: {}", 
                    userId, imageGenerateDTO.getAnalysisId(), e.getMessage(), e);
            throw new BaseException("图片生成失败: " + e.getMessage());
        }
        
        // 保存生成结果到数据库
        List<GeneratedImage> generatedImages = new ArrayList<>();
        List<Long> generatedImageIds = new ArrayList<>();
        List<ImageVO> imageInfos = new ArrayList<>();

        for (String generatedImageUrl : generatedImageUrls) {
            GeneratedImage generatedImage = new GeneratedImage();
            generatedImage.setAnalysisId(imageGenerateDTO.getAnalysisId());
            generatedImage.setAnalysisResult(analysis.getAnalysisResult());
            generatedImage.setImageUrl(generatedImageUrl);
            generatedImage.setGenerateTime(new Date());

            // 保存到数据库
            imageMapper.insertGeneratedImage(generatedImage);

            generatedImages.add(generatedImage);
            generatedImageIds.add(generatedImage.getGenImageId());

            // 构建返回的图片信息
            ImageVO imageInfo = new ImageVO();
            imageInfo.setImageId(generatedImage.getGenImageId());
            imageInfo.setImageUrl(generatedImage.getImageUrl());
            imageInfos.add(imageInfo);
        }
        
        log.info("生成风格化图片完成，用户ID: {}, 分析ID: {}, 生成图片数量: {}", 
                userId, imageGenerateDTO.getAnalysisId(), generatedImages.size());
        
        return ImageGenerateVO.builder()
                .generatedImageIds(generatedImageIds)
                .images(imageInfos)
                .build();
    }
    
    @Override
    @Transactional
    public ImageSelectVO selectImage(ImageSelectDTO imageSelectDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 验证生成图片是否存在
        GeneratedImage generatedImage = imageMapper.getGeneratedImageById(imageSelectDTO.getGenImageId());
        if (generatedImage == null) {
            throw new BaseException("生成图片不存在");
        }
        
        // 验证生成图片是否属于当前用户（通过分析结果验证）
        MultimodalAnalysis analysis = imageMapper.getAnalysisById(generatedImage.getAnalysisId());
        if (analysis == null || !analysis.getUserId().equals(userId)) {
            throw new BaseException("无权限访问该生成图片");
        }
        
        // 创建用户选择记录
        UserSelection userSelection = new UserSelection();
        userSelection.setUserId(userId);
        userSelection.setGenImageId(imageSelectDTO.getGenImageId());
        userSelection.setSelectTime(LocalDateTime.now());
        
        // 插入用户选择记录
        imageMapper.insertUserSelection(userSelection);
        
        log.info("用户选择图片成功，用户ID: {}, 生成图片ID: {}, 选择记录ID: {}", 
                userId, imageSelectDTO.getGenImageId(), userSelection.getSelectionId());
        
        return ImageSelectVO.builder()
                .selectionId(userSelection.getSelectionId())
                .build();
    }
    
    @Override
    @Transactional
    public SingleImageUploadVO singleUploadImage(SingleImageUploadDTO singleImageUploadDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        
        // 验证图片类型
        if (singleImageUploadDTO.getImageType() == null || 
            (singleImageUploadDTO.getImageType() != 1 && singleImageUploadDTO.getImageType() != 2)) {
            throw new BaseException("图片类型错误，只支持1=人物照，2=看展照");
        }
        
        // 验证文件
        if (singleImageUploadDTO.getImage() == null || singleImageUploadDTO.getImage().isEmpty()) {
            throw new BaseException("图片文件不能为空");
        }
        
        // 上传图片文件
        String imageUrl = upload(singleImageUploadDTO.getImage());
        
        // 创建上传图片记录
        UploadedImage uploadedImage = new UploadedImage();
        uploadedImage.setUserId(userId);
        uploadedImage.setImageType(singleImageUploadDTO.getImageType());
        uploadedImage.setImageUrl(imageUrl);
        uploadedImage.setUploadTime(LocalDateTime.now());
        
        // 保存到数据库
        imageMapper.insert(uploadedImage);
        
        log.info("单文件图片上传成功，用户ID: {}, 图片类型: {}, 图片ID: {}", 
                userId, singleImageUploadDTO.getImageType(), uploadedImage.getImageId());
        
        // 构建返回结果
        return SingleImageUploadVO.builder()
                .imageId(uploadedImage.getImageId())
                .imageUrl(uploadedImage.getImageUrl())
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
