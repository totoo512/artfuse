package com.zyy.service.impl;

import com.zyy.context.BaseContext;
import com.zyy.dto.TextGenerateDTO;
import com.zyy.entity.GeneratedImage;
import com.zyy.entity.GeneratedText;
import com.zyy.entity.UserSelection;
import com.zyy.mapper.ImageMapper;
import com.zyy.mapper.TextMapper;
import com.zyy.service.TextService;
import com.zyy.utils.DouBaoSeedUtil;
import com.zyy.vo.TextGenerateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 文本生成服务实现类
 */
@Slf4j
@Service
public class TextServiceImpl implements TextService {

    @Autowired
    private ImageMapper imageMapper;

    @Autowired
    private TextMapper textMapper;

    @Autowired
    private DouBaoSeedUtil douBaoSeedUtil;

    @Override
    public TextGenerateVO generateText(TextGenerateDTO textGenerateDTO) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();

        // 1. 根据selectionId查询用户选择记录
        UserSelection userSelection = imageMapper.getUserSelectionById(textGenerateDTO.getSelectionId());
        if (userSelection == null) {
            throw new RuntimeException("用户选择记录不存在");
        }

        // 2. 验证选择记录是否属于当前用户
        if (!userSelection.getUserId().equals(userId)) {
            throw new RuntimeException("无权限访问该选择记录");
        }

        // 3. 根据selectionId查询对应的生成图片信息
        Long genImageId = userSelection.getGenImageId();
        GeneratedImage generatedImage = imageMapper.getGeneratedImageById(genImageId);
        if (generatedImage == null) {
            throw new RuntimeException("生成图片记录不存在");
        }

        // 4. 获取图片URL列表
        List<String> imageUrls = Collections.singletonList(generatedImage.getImageUrl());

        // 5. 调用豆包生成朋友圈文案（基于图片内容）
        String prompt = """
                你是一位专为社交媒体（如朋友圈、Instagram）创作文案的创意大师。你深谙如何将艺术氛围与个人情感融为一体，文字风格凝练、高级且富有感染力。
                任务描述 (Task Description):
                你的任务是为一张充满艺术想象力的图片创作4条简短而精彩的文案。这张图片将用户的个人形象与艺术品的风格进行了奇妙的融合，仿佛用户本人走进了画中世界。你需要基于图片传递出的情绪、色彩、风格和构图，生成多样化的文案选项，激发观看者的共鸣与好奇。
                输入 (Input):
                [此处传入用户选择的那一张风格化图片]
                负向提示词 (Negative Prompts):
                绝对禁止 (Strictly Forbidden): 在文案中提及 “AI”、“人工智能”、“生成”、“算法”、“模型”、“数字”、“技术”、“一键” 或任何暗示图片是由机器创造的词汇。
                核心焦点 (Core Focus): 文案应完全聚焦于 艺术体验、个人情感、梦境、想象力 和 画面本身，让它看起来像是一次真实的、沉浸式的艺术创作或奇幻经历。
                输出要求 (Output Requirements):
                严格格式化 (Strict Formatting): 直接返回4条文案，每条占一行，不要添加任何序号、标题、解释或多余的文字。
                内容多样性 (Content Diversity):
                中文文案一: 偏向诗意或感性，侧重于艺术与自我的深度融合感受。
                中文文案二: 更具个性和态度，可以是从一个有趣的视角解读这次艺术体验，或是表达一种不羁的艺术宣言。
                英文文案: 简洁、时尚（catchy & chic），适合在国际化社交平台发布。
                小语种文案: 根据图片的艺术风格，智能选择一种最贴切的小语种（如：穆夏风格对应法语，浮世绘风格对应日语，弗里达风格对应西班牙语等），并创作一句简短文案。如果风格不明显，优先选择法语或日语。
                长度限制 (Length Constraint):
                每一条文案的长度都必须严格控制在 30个Token 以内，确保内容精炼、易读。
                示例 (Example):
                假设输入的图片是用户形象与梵高《星空》风格的融合，那么理想的输出应该类似这样：
                今夜，我在自己的星空里漫游。
                艺术上头，后劲有点大。
                Lost in my own starry night.
                La nuit étoilée, c'est moi. (法语：星夜，就是我。)
                """;
        String momentsCopy;
        try {
            momentsCopy = douBaoSeedUtil.chatWithImages(prompt, imageUrls);
            log.info("豆包生成文案成功，selectionId: {}, genImageId: {}, 文案: {}", textGenerateDTO.getSelectionId(), genImageId, momentsCopy);
        } catch (Exception e) {
            log.error("豆包生成文案失败，selectionId: {}, genImageId: {}", textGenerateDTO.getSelectionId(), genImageId, e);
            throw new RuntimeException("文案生成失败，请稍后重试");
        }

        // 6. 保存生成结果到数据库
        GeneratedText generatedText = new GeneratedText();
        generatedText.setSelectionId(textGenerateDTO.getSelectionId());
        generatedText.setMomentsCopy(momentsCopy);
        generatedText.setGenerateTime(LocalDateTime.now());

        textMapper.insertGeneratedText(generatedText);

        // 5. 返回结果
        return TextGenerateVO.builder()
                .textId(generatedText.getTextId())
                .momentsCopy(momentsCopy)
                .build();
    }

}