package com.zyy.mapper;

import com.zyy.entity.MultimodalAnalysis;
import com.zyy.entity.UploadedImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImageMapper {

    /**
     * 插入单个上传图片记录
     * @param uploadedImage
     */
    @Insert("insert into uploaded_image(user_id, image_type, image_url, upload_time) " +
            "values(#{userId}, #{imageType}, #{imageUrl}, #{uploadTime})")
    @Options(useGeneratedKeys = true, keyProperty = "imageId")
    void insert(UploadedImage uploadedImage);

    /**
     * 批量插入上传图片记录
     * @param uploadedImages
     */
    void insertBatch(List<UploadedImage> uploadedImages);

    /**
     * 根据图片ID查询上传图片记录
     * @param imageId
     * @return
     */
    @Select("select * from uploaded_image where image_id = #{imageId}")
    UploadedImage getById(Long imageId);
    
    /**
     * 插入多模态分析结果
     * @param multimodalAnalysis
     */
    @Insert("insert into multimodal_analysis(user_id, portrait_image_id, exhibition_image_ids, analysis_result, analysis_time) " +
            "values(#{userId}, #{portraitImageId}, #{exhibitionImageIds}, #{analysisResult}, #{analysisTime})")
    @Options(useGeneratedKeys = true, keyProperty = "analysisId")
    void insertAnalysis(MultimodalAnalysis multimodalAnalysis);
}
