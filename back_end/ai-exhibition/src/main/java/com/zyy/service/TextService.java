package com.zyy.service;

import com.zyy.dto.TextGenerateDTO;
import com.zyy.vo.TextGenerateVO;

/**
 * 文本生成服务接口
 */
public interface TextService {

    /**
     * 生成朋友圈文案
     * @param textGenerateDTO 文本生成请求参数
     * @return 生成的文本结果
     */
    TextGenerateVO generateText(TextGenerateDTO textGenerateDTO);

}