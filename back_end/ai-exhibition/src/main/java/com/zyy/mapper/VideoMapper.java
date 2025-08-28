package com.zyy.mapper;

import com.zyy.entity.VideoTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
    
    /**
     * 查询所有未完成的视频任务（状态不为3=完成）
     * @return 未完成的视频任务列表
     */
    @Select("SELECT task_id, user_id, selection_id, ref_image_id, video_prompt, status, video_url, error_msg, create_time, update_time " +
            "FROM video_task WHERE status != 3 ORDER BY create_time DESC")
    List<VideoTask> selectUnfinishedVideoTasks();
    
    /**
     * 查询所有未完成的视频任务及其关联的图片URL
     * @return 未完成的视频任务及图片URL列表
     */
    @Select("SELECT vt.task_id, vt.user_id, vt.selection_id, vt.ref_image_id, vt.video_prompt, vt.status, " +
            "vt.video_url, vt.error_msg, vt.create_time, vt.update_time, " +
            "gi.image_url AS gen_image_url, ui.image_url AS ref_image_url " +
            "FROM video_task vt " +
            "LEFT JOIN user_selection us ON vt.selection_id = us.selection_id " +
            "LEFT JOIN generated_image gi ON us.gen_image_id = gi.gen_image_id " +
            "LEFT JOIN uploaded_image ui ON vt.ref_image_id = ui.image_id " +
            "WHERE vt.status != 3 ORDER BY vt.create_time DESC")
    List<Map<String, Object>> selectUnfinishedVideoTasksWithImages();
}