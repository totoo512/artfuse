package com.zyy.mapper;

import com.zyy.entity.GeneratedText;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * 文本生成数据访问层接口
 */
@Mapper
public interface TextMapper {

    /**
     * 插入生成的文本记录
     * @param generatedText 生成的文本实体
     */
    @Insert("INSERT INTO generated_text (selection_id, moments_copy, generate_time) " +
            "VALUES (#{selectionId}, #{momentsCopy}, #{generateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "textId")
    void insertGeneratedText(GeneratedText generatedText);

}