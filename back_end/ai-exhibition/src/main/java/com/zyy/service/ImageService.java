package com.zyy.service;

import com.zyy.dto.*;
import com.zyy.vo.ImageAnalysisVO;
import com.zyy.vo.ImageGenerateVO;
import com.zyy.vo.ImageSelectVO;
import com.zyy.vo.ImageUploadVO;
import com.zyy.vo.SingleImageUploadVO;
import com.zyy.vo.GeneratePostcardVO;

public interface ImageService {
    ImageUploadVO uploadImages(ImageUploadDTO imageUploadDTO);
    
    ImageAnalysisVO analyzeImages(ImageAnalysisDTO imageAnalysisDTO);
    
    ImageGenerateVO generateImages(ImageGenerateDTO imageGenerateDTO);
    
    ImageSelectVO selectImage(ImageSelectDTO imageSelectDTO);
    
    SingleImageUploadVO singleUploadImage(SingleImageUploadDTO singleImageUploadDTO);

    void updateImageFavorite(ImageFavoriteDTO imageFavoriteDTO);
    
    GeneratePostcardVO generatePostcard(GeneratePostcardDTO generatePostcardDTO);
}
