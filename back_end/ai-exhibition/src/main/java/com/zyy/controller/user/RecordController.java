package com.zyy.controller.user;

import com.zyy.dto.ImageDeleteDTO;
import com.zyy.dto.ImageFavoriteDTO;
import com.zyy.dto.RecordCreateDTO;
import com.zyy.dto.RecordDeleteDTO;
import com.zyy.dto.RecordUpdateDTO;
import com.zyy.result.Result;
import com.zyy.service.RecordService;
import com.zyy.vo.RecordCreateVO;
import com.zyy.vo.RecordListVO;
import com.zyy.vo.RecordDetailVO;
import com.zyy.result.PageResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/record")
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;

    /**
     * 新建看展记录接口
     * @param recordCreateDTO
     * @return
     */
    @PostMapping("/create")
    @ApiOperation("新建看展记录接口")
    public Result<RecordCreateVO> createRecord(@RequestBody @Valid RecordCreateDTO recordCreateDTO) {
        log.info("新建看展记录接口: {}", recordCreateDTO);
        
        RecordCreateVO recordCreateVO = recordService.createRecord(recordCreateDTO);
        
        return Result.success(recordCreateVO);
    }

    /**
     * 编辑看展记录接口
     * @param recordUpdateDTO
     * @return
     */
    @PutMapping("/update")
    @ApiOperation("编辑看展记录接口")
    public Result<Object> updateRecord(@RequestBody @Valid RecordUpdateDTO recordUpdateDTO) {
        log.info("编辑看展记录接口: {}", recordUpdateDTO);
        
        recordService.updateRecord(recordUpdateDTO);
        
        return Result.success();
    }

    /**
     * 删除看展记录图片接口
     * @param imageDeleteDTO
     * @return
     */
    @DeleteMapping("/image/delete")
    @ApiOperation("删除看展记录图片接口")
    public Result<Object> deleteImage(@RequestBody @Valid ImageDeleteDTO imageDeleteDTO) {
        log.info("删除看展记录图片接口: {}", imageDeleteDTO);
        
        recordService.deleteImage(imageDeleteDTO);
        
        return Result.success();
    }

    /**
     * 删除看展记录接口
     * @param recordDeleteDTO
     * @return
     */
    @DeleteMapping("/delete")
    @ApiOperation("删除看展记录接口")
    public Result<Object> deleteRecord(@RequestBody @Valid RecordDeleteDTO recordDeleteDTO) {
        log.info("删除看展记录接口: {}", recordDeleteDTO);
        
        recordService.deleteRecord(recordDeleteDTO);
        
        return Result.success();
    }

    /**
     * 获取看展记录列表接口
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("获取看展记录列表接口")
    public Result<PageResult> getRecordList(@RequestParam(defaultValue = "1") Integer pageNum,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("获取看展记录列表接口: pageNum={}, pageSize={}", pageNum, pageSize);
        
        PageResult pageResult = recordService.getRecordList(pageNum, pageSize);
        
        return Result.success(pageResult);
    }

    /**
     * 获取看展记录详情接口
     * @param recordId 记录ID
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation("获取看展记录详情接口")
    public Result<RecordDetailVO> getRecordDetail(@RequestParam String recordId) {
        log.info("获取看展记录详情接口: recordId={}", recordId);
        
        RecordDetailVO recordDetailVO = recordService.getRecordDetail(recordId);
        
        return Result.success(recordDetailVO);
    }
}