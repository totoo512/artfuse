package com.zyy.service;

import com.zyy.dto.ImageDeleteDTO;
import com.zyy.dto.ImageFavoriteDTO;
import com.zyy.dto.RecordCreateDTO;
import com.zyy.dto.RecordDeleteDTO;
import com.zyy.dto.RecordUpdateDTO;
import com.zyy.vo.RecordCreateVO;
import com.zyy.vo.RecordListVO;
import com.zyy.vo.RecordDetailVO;
import com.zyy.result.PageResult;

public interface RecordService {
    
    /**
     * 创建看展记录
     * @param recordCreateDTO
     * @return
     */
    RecordCreateVO createRecord(RecordCreateDTO recordCreateDTO);

    /**
     * 编辑看展记录
     * @param recordUpdateDTO
     */
    void updateRecord(RecordUpdateDTO recordUpdateDTO);

    /**
     * 删除看展记录图片
     * @param imageDeleteDTO
     */
    void deleteImage(ImageDeleteDTO imageDeleteDTO);

    /**
     * 删除看展记录
     * @param recordDeleteDTO
     */
    void deleteRecord(RecordDeleteDTO recordDeleteDTO);

    /**
     * 获取看展记录列表
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return
     */
    PageResult getRecordList(Integer pageNum, Integer pageSize);

    /**
     * 获取看展记录详情
     * @param recordId 记录ID
     * @return
     */
    RecordDetailVO getRecordDetail(String recordId);

    /**
     * 更新图片收藏状态
     * @param imageFavoriteDTO
     */
    void updateImageFavorite(ImageFavoriteDTO imageFavoriteDTO);
}