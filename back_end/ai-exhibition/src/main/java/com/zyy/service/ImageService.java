package com.zyy.service;

import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageGenerateDTO;
import com.zyy.dto.ImageSelectDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.dto.SingleImageUploadDTO;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageGenerateVO;
import com.zyy.vo.ImageSelectVO;
import com.zyy.vo.ImageUploadVO;
import com.zyy.vo.SingleImageUploadVO;

public interface ImageService {
    ImageUploadVO uploadImages(ImageUploadDTO imageUploadDTO);
    
    ImageAnalysisVO analyzeImages(ImageAnalysisDTO imageAnalysisDTO);
    
    ImageGenerateVO generateImages(ImageGenerateDTO imageGenerateDTO);
    
    ImageSelectVO selectImage(ImageSelectDTO imageSelectDTO);
    
    SingleImageUploadVO singleUploadImage(SingleImageUploadDTO singleImageUploadDTO);
}
