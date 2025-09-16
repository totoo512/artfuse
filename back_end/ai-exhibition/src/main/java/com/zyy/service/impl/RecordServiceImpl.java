package com.zyy.service.impl;

import com.zyy.context.BaseContext;
import com.zyy.dto.ImageDeleteDTO;
import com.zyy.dto.ImageFavoriteDTO;
import com.zyy.dto.RecordCreateDTO;
import com.zyy.dto.RecordDeleteDTO;
import com.zyy.dto.RecordUpdateDTO;
import com.zyy.entity.ExhibitionRecord;
import com.zyy.exception.BaseException;
import com.zyy.mapper.RecordMapper;
import com.zyy.service.RecordService;
import com.zyy.vo.RecordCreateVO;
import com.zyy.vo.RecordListVO;
import com.zyy.vo.RecordDetailVO;
import com.zyy.vo.ImageDetailVO;
import com.zyy.result.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    @Transactional
    public RecordCreateVO createRecord(RecordCreateDTO recordCreateDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("创建看展记录，用户ID: {}, 展览名称: {}", userId, recordCreateDTO.getTitle());
        
        // 创建ExhibitionRecord实体
        ExhibitionRecord exhibitionRecord = new ExhibitionRecord();
        exhibitionRecord.setUserId(userId);
        exhibitionRecord.setTitle(recordCreateDTO.getTitle());
        exhibitionRecord.setGalleryName(recordCreateDTO.getGalleryName());
        
        // 处理展览日期，如果为空则使用当前日期
        LocalDateTime exhibitionDate;
        if (StringUtils.hasText(recordCreateDTO.getExhibitionDate())) {
            LocalDate date = LocalDate.parse(recordCreateDTO.getExhibitionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            exhibitionDate = date.atStartOfDay();
        } else {
            exhibitionDate = LocalDate.now().atStartOfDay();
        }
        exhibitionRecord.setExhibitionDate(exhibitionDate);
        
        exhibitionRecord.setCountry(recordCreateDTO.getCountry());
        exhibitionRecord.setCity(recordCreateDTO.getCity());
        exhibitionRecord.setArtist(recordCreateDTO.getArtist());
        exhibitionRecord.setStyleTags(recordCreateDTO.getStyleTags());
        exhibitionRecord.setNotes(recordCreateDTO.getNotes());
        exhibitionRecord.setAiSummary(recordCreateDTO.getAiSummary());
        
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        exhibitionRecord.setCreateTime(now);
        exhibitionRecord.setUpdateTime(now);
        
        // 插入数据库
        recordMapper.insert(exhibitionRecord);
        
        log.info("看展记录创建成功，记录ID: {}", exhibitionRecord.getRecordId());
        
        // 构建返回结果
        return RecordCreateVO.builder()
                .recordId(exhibitionRecord.getRecordId())
                .build();
    }

    @Override
    @Transactional
    public void updateRecord(RecordUpdateDTO recordUpdateDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("编辑看展记录，用户ID: {}, 记录ID: {}", userId, recordUpdateDTO.getRecordId());
        
        // 创建ExhibitionRecord实体用于更新
        ExhibitionRecord exhibitionRecord = new ExhibitionRecord();
        exhibitionRecord.setRecordId(recordUpdateDTO.getRecordId());
        exhibitionRecord.setUserId(userId); // 确保只能更新自己的记录
        
        // 直接设置所有字段，由动态SQL处理非空字段更新
        exhibitionRecord.setTitle(recordUpdateDTO.getTitle());
        exhibitionRecord.setGalleryName(recordUpdateDTO.getGalleryName());
        
        // 处理展览日期
        if (StringUtils.hasText(recordUpdateDTO.getExhibitionDate())) {
            LocalDate date = LocalDate.parse(recordUpdateDTO.getExhibitionDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            exhibitionRecord.setExhibitionDate(date.atStartOfDay());
        }
        
        exhibitionRecord.setCountry(recordUpdateDTO.getCountry());
        exhibitionRecord.setCity(recordUpdateDTO.getCity());
        exhibitionRecord.setArtist(recordUpdateDTO.getArtist());
        exhibitionRecord.setStyleTags(recordUpdateDTO.getStyleTags());
        exhibitionRecord.setNotes(recordUpdateDTO.getNotes());
        exhibitionRecord.setAiSummary(recordUpdateDTO.getAiSummary());
        exhibitionRecord.setCoverImageUrl(recordUpdateDTO.getCoverImageUrl());
        
        // 设置更新时间
        exhibitionRecord.setUpdateTime(LocalDateTime.now());
        
        // 更新数据库
        recordMapper.update(exhibitionRecord);
        
        log.info("看展记录编辑成功，记录ID: {}", recordUpdateDTO.getRecordId());
    }

    @Override
    @Transactional
    public void deleteImage(ImageDeleteDTO imageDeleteDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("删除看展记录图片，用户ID: {}, 记录ID: {}, 图片ID: {}", userId, imageDeleteDTO.getRecordId(), imageDeleteDTO.getImageId());
        
        // 调用Mapper删除图片，确保权限验证
        int deletedRows = recordMapper.deleteImage(Long.parseLong(imageDeleteDTO.getImageId()), Long.parseLong(imageDeleteDTO.getRecordId()), userId);
        
        if (deletedRows == 0) {
            log.error("删除图片失败，可能是权限不足或图片不存在，用户ID: {}, 记录ID: {}, 图片ID: {}", userId, imageDeleteDTO.getRecordId(), imageDeleteDTO.getImageId());
            throw new BaseException("删除图片失败，权限不足或图片不存在");
        }
        
        log.info("看展记录图片删除成功，记录ID: {}, 图片ID: {}", imageDeleteDTO.getRecordId(), imageDeleteDTO.getImageId());
    }

    @Override
    @Transactional
    public void deleteRecord(RecordDeleteDTO recordDeleteDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("删除看展记录，用户ID: {}, 记录ID: {}", userId, recordDeleteDTO.getRecordId());
        
        // 调用Mapper删除记录，确保权限验证和级联删除
        int deletedRows = recordMapper.deleteRecord(recordDeleteDTO.getRecordId(), userId);
        
        if (deletedRows == 0) {
            log.error("删除看展记录失败，可能是权限不足或记录不存在，用户ID: {}, 记录ID: {}", userId, recordDeleteDTO.getRecordId());
            throw new BaseException("删除看展记录失败，权限不足或记录不存在");
        }
        
        log.info("看展记录删除成功，记录ID: {}", recordDeleteDTO.getRecordId());
    }

    @Override
    public PageResult getRecordList(Integer pageNum, Integer pageSize) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("获取看展记录列表，用户ID: {}, 页码: {}, 每页大小: {}", userId, pageNum, pageSize);
        
        // 开启分页
        PageHelper.startPage(pageNum, pageSize);
        
        // 查询当前用户的记录列表
        List<RecordListVO> recordList = recordMapper.selectRecordList(userId);
        
        // 封装分页结果
        PageInfo<RecordListVO> pageInfo = new PageInfo<>(recordList);
        
        // 使用PageResult封装返回数据
        PageResult pageResult = new PageResult(pageInfo.getTotal(), recordList);
        
        log.info("获取看展记录列表成功，用户ID: {}, 总记录数: {}", userId, pageInfo.getTotal());
        
        return pageResult;
    }

    @Override
    public RecordDetailVO getRecordDetail(String recordId) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("获取看展记录详情，用户ID: {}, 记录ID: {}", userId, recordId);
        
        // 查询记录详情
        RecordDetailVO.RecordInfo recordInfo = recordMapper.selectRecordDetail(Long.parseLong(recordId), userId);
        
        if (recordInfo == null) {
            log.error("记录不存在或无权限访问，用户ID: {}, 记录ID: {}", userId, recordId);
            throw new BaseException("记录不存在或无权限访问");
        }
        
        // 查询图片列表（按收藏置顶+时间倒序排序）
        List<ImageDetailVO> imageList = recordMapper.selectImageList(Long.parseLong(recordId), userId);
        
        // 构建返回结果
        RecordDetailVO recordDetailVO = new RecordDetailVO();
        recordDetailVO.setRecordInfo(recordInfo);
        recordDetailVO.setImageList(imageList);
        
        log.info("获取看展记录详情成功，用户ID: {}, 记录ID: {}, 图片数量: {}", userId, recordId, imageList.size());
        
        return recordDetailVO;
    }

    @Override
    @Transactional
    public void updateImageFavorite(ImageFavoriteDTO imageFavoriteDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        log.info("更新图片收藏状态，用户ID: {}, 图片ID: {}, 收藏状态: {}", userId, imageFavoriteDTO.getImageId(), imageFavoriteDTO.getIsFavorite());
        
        // 调用Mapper更新图片收藏状态，确保权限验证
        int updatedRows = recordMapper.updateImageFavorite(Long.parseLong(imageFavoriteDTO.getImageId()), imageFavoriteDTO.getIsFavorite(), userId);
        
        if (updatedRows == 0) {
            log.error("更新图片收藏状态失败，可能是权限不足或图片不存在，用户ID: {}, 图片ID: {}", userId, imageFavoriteDTO.getImageId());
            throw new BaseException("更新图片收藏状态失败，权限不足或图片不存在");
        }
        
        log.info("图片收藏状态更新成功，用户ID: {}, 图片ID: {}, 收藏状态: {}", userId, imageFavoriteDTO.getImageId(), imageFavoriteDTO.getIsFavorite());
    }
}