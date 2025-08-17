package com.zyy.mapper;

import com.zyy.entity.GeneratedImage;
import com.zyy.entity.MultimodalAnalysis;
import com.zyy.entity.UploadedImage;
import com.zyy.entity.UserSelection;
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
    
    /**
     * 插入生成图片记录
     * @param generatedImage
     */
    @Insert("insert into generated_image(analysis_id, analysis_result, image_url, generate_time) " +
            "values(#{analysisId}, #{analysisResult}, #{imageUrl}, #{generateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "genImageId")
    void insertGeneratedImage(GeneratedImage generatedImage);
    
    /**
     * 批量插入生成图片记录
     * @param generatedImages
     */
    void insertGeneratedImageBatch(List<GeneratedImage> generatedImages);
    
    /**
     * 根据分析ID查询多模态分析结果
     * @param analysisId
     * @return
     */
    @Select("select * from multimodal_analysis where analysis_id = #{analysisId}")
    MultimodalAnalysis getAnalysisById(Long analysisId);
    
    /**
     * 根据生成图片ID查询生成图片记录
     * @param genImageId
     * @return
     */
    @Select("select * from generated_image where gen_image_id = #{genImageId}")
    GeneratedImage getGeneratedImageById(Long genImageId);
    
    /**
     * 插入用户选择记录
     * @param userSelection
     */
    @Insert("insert into user_selection(user_id, gen_image_id, select_time) " +
            "values(#{userId}, #{genImageId}, #{selectTime})")
    @Options(useGeneratedKeys = true, keyProperty = "selectionId")
    void insertUserSelection(UserSelection userSelection);
    
    /**
     * 根据选择ID查询用户选择记录
     * @param selectionId
     * @return
     */
    @Select("select * from user_selection where selection_id = #{selectionId}")
    UserSelection getUserSelectionById(Long selectionId);
}
