package com.zyy.mapper;

import com.zyy.entity.VideoTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

/**
 * 视频生成Mapper接口
 */
@Mapper
public interface VideoMapper {

    /**
     * 插入视频任务
     * @param videoTask 视频任务实体
     */
    @Insert("INSERT INTO video_task (user_id, selection_id, ref_image_id, video_prompt, status, create_time, update_time) " +
            "VALUES (#{userId}, #{selectionId}, #{refImageId}, #{videoPrompt}, #{status}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "taskId")
    void insertVideoTask(VideoTask videoTask);

    /**
     * 根据任务ID和用户ID查询视频任务
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 视频任务实体
     */
    @Select("SELECT task_id, user_id, selection_id, ref_image_id, video_prompt, status, video_url, error_msg, create_time, update_time " +
            "FROM video_task WHERE task_id = #{taskId} AND user_id = #{userId}")
    VideoTask selectVideoTaskByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    /**
     * 更新视频任务URL和状态
     * @param taskId 任务ID
     * @param videoUrl 视频URL
     * @param status 状态
     * @param updateTime 更新时间
     */
    @Update("UPDATE video_task SET video_url = #{videoUrl}, status = #{status}, update_time = #{updateTime} " +
            "WHERE task_id = #{taskId}")
    void updateVideoTaskUrl(@Param("taskId") Long taskId, @Param("videoUrl") String videoUrl, 
                           @Param("status") Integer status, @Param("updateTime") java.time.LocalDateTime updateTime);

    /**
     * 根据任务ID查询视频任务（不验证用户ID）
     * @param taskId 任务ID
     * @return 视频任务实体
     */
    @Select("SELECT task_id, user_id, selection_id, ref_image_id, video_prompt, status, video_url, error_msg, create_time, update_time " +
            "FROM video_task WHERE task_id = #{taskId}")
    VideoTask selectVideoTaskByTaskId(@Param("taskId") Long taskId);
}