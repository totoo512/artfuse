package com.zyy.service;

import com.zyy.dto.ImageAnalysisDTO;
import com.zyy.dto.ImageUploadDTO;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageUploadVO;

public interface ImageService {
    ImageUploadVO uploadImages(ImageUploadDTO imageUploadDTO);
    
    ImageAnalysisVO analyzeImages(ImageAnalysisDTO imageAnalysisDTO);
}
