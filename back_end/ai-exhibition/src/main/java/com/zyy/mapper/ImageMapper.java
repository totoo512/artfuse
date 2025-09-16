package com.zyy.mapper;

import com.zyy.entity.AigcStyle;
import com.zyy.entity.AigcWork;
import com.zyy.entity.MultimodalAnalysis;
import com.zyy.entity.UploadedImage;
import com.zyy.entity.UserSelection;
import org.apache.ibatis.annotations.*;

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
     * @param aigcWork
     */
    @Insert("insert into aigc_work(user_id, exhibition_record_id, style_id, creation_type, source_image_ids, work_name, is_collected, analysis_id, analysis_result, url, generate_time) " +
            "values(#{userId}, #{exhibitionRecordId}, #{styleId}, #{creationType}, #{sourceImageIds}, #{workName}, #{isCollected}, #{analysisId}, #{analysisResult}, #{url}, #{generateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "workId")
    void insertGeneratedImage(AigcWork aigcWork);
    
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
    @Select("select * from aigc_work where work_id = #{genImageId}")
    AigcWork getGeneratedImageById(Long genImageId);
    
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

    /**
     * 更新图片收藏状态（确保权限验证）
     * @param imageId 图片ID
     * @param isFavorite 收藏状态 0=未收藏，1=已收藏
     * @param userId 用户ID
     * @return 更新的行数
     */
    @Update("UPDATE uploaded_image SET is_favorite = #{isFavorite} WHERE image_id = #{imageId} AND user_id = #{userId}")
    int updateImageFavorite(@Param("imageId") Long imageId, @Param("isFavorite") Integer isFavorite, @Param("userId") Long userId);
    
    /**
     * 根据图片ID列表查询上传图片记录
     * @param imageIds
     * @return
     */
    @Select("<script>" +
            "select * from uploaded_image where image_id in " +
            "<foreach collection='imageIds' item='imageId' open='(' separator=',' close=')'>" +
            "#{imageId}" +
            "</foreach>" +
            "</script>")
    List<UploadedImage> getByIds(@Param("imageIds") List<Long> imageIds);
    
    /**
     * 根据风格ID查询AIGC风格信息
     * @param styleId
     * @return
     */
    @Select("select * from aigc_style where style_id = #{styleId}")
    AigcStyle getStyleById(Integer styleId);
}
