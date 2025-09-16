package com.zyy.mapper;

import com.zyy.entity.ExhibitionRecord;
import com.zyy.vo.RecordListVO;
import com.zyy.vo.RecordDetailVO;
import com.zyy.vo.ImageDetailVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.util.List;

@Mapper
public interface RecordMapper {

    /**
     * 插入看展记录
     * @param exhibitionRecord
     */
    @Insert("insert into exhibition_record(user_id, title, gallery_name, exhibition_date, country, city, artist, style_tags, notes, ai_summary, create_time, update_time) " +
            "values(#{userId}, #{title}, #{galleryName}, #{exhibitionDate}, #{country}, #{city}, #{artist}, #{styleTags}, #{notes}, #{aiSummary}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "recordId")
    void insert(ExhibitionRecord exhibitionRecord);

    /**
     * 根据记录ID查询看展记录
     * @param recordId
     * @return
     */
    @Select("select * from exhibition_record where record_id = #{recordId}")
    ExhibitionRecord getById(Long recordId);

    /**
     * 更新展览记录
     * @param exhibitionRecord
     */
    void update(ExhibitionRecord exhibitionRecord);

    /**
     * 删除看展记录图片（确保权限验证）
     * @param imageId 图片ID
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 删除的行数
     */
    @Delete("DELETE FROM uploaded_image WHERE image_id = #{imageId} AND exhibition_record_id = #{recordId} AND user_id = #{userId}")
    int deleteImage(@Param("imageId") Long imageId, @Param("recordId") Long recordId, @Param("userId") Long userId);

    /**
     * 删除看展记录（确保权限验证和级联删除）
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 删除的行数
     */
    @Delete("DELETE FROM exhibition_record WHERE record_id = #{recordId} AND user_id = #{userId}")
    int deleteRecord(@Param("recordId") Long recordId, @Param("userId") Long userId);

    /**
     * 查询当前用户的看展记录列表
     * @param userId 用户ID
     * @return 记录列表
     */
    @Select("SELECT record_id as recordId, cover_image_url as coverImageUrl, title, gallery_name as galleryName, " +
            "DATE_FORMAT(exhibition_date, '%Y-%m-%d') as exhibitionDate " +
            "FROM exhibition_record WHERE user_id = #{userId} ORDER BY exhibition_date DESC")
    List<RecordListVO> selectRecordList(@Param("userId") Long userId);

    /**
     * 查询看展记录详情（确保权限验证）
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 记录详情
     */
    @Select("SELECT record_id as recordId, cover_image_url as coverImageUrl, title, gallery_name as galleryName, " +
            "DATE_FORMAT(exhibition_date, '%Y-%m-%d') as exhibitionDate " +
            "FROM exhibition_record WHERE record_id = #{recordId} AND user_id = #{userId}")
    RecordDetailVO.RecordInfo selectRecordDetail(@Param("recordId") Long recordId, @Param("userId") Long userId);

    /**
     * 查询记录的图片列表（按收藏置顶+时间倒序排序）
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 图片列表
     */
    @Select("SELECT image_id as imageId, image_url as imageUrl, is_favorite as isFavorite, " +
            "personal_note as personalNote, DATE_FORMAT(upload_time, '%Y-%m-%d %H:%i:%s') as uploadTime " +
            "FROM uploaded_image WHERE exhibition_record_id = #{recordId} AND user_id = #{userId} " +
            "ORDER BY is_favorite DESC, upload_time DESC")
    List<ImageDetailVO> selectImageList(@Param("recordId") Long recordId, @Param("userId") Long userId);

    /**
     * 更新图片收藏状态（确保权限验证）
     * @param imageId 图片ID
     * @param isFavorite 收藏状态 0=未收藏，1=已收藏
     * @param userId 用户ID
     * @return 更新的行数
     */
    @Update("UPDATE uploaded_image SET is_favorite = #{isFavorite} WHERE image_id = #{imageId} AND user_id = #{userId}")
    int updateImageFavorite(@Param("imageId") Long imageId, @Param("isFavorite") Integer isFavorite, @Param("userId") Long userId);
}