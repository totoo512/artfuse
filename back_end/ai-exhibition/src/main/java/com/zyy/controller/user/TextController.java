package com.zyy.controller.user;

import com.zyy.dto.TextGenerateDTO;
import com.zyy.result.Result;
import com.zyy.service.TextService;
import com.zyy.vo.TextGenerateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user/text")
@Api(tags = "用户文本生成相关接口")
@Slf4j
public class TextController {

    @Autowired
    private TextService textService;

    /**
     * 生成朋友圈文案
     * @param textGenerateDTO 文本生成请求参数
     * @return 生成的文本结果
     */
    @PostMapping("/generate")
    @ApiOperation("生成朋友圈文案")
    public Result<TextGenerateVO> generateText(@RequestBody TextGenerateDTO textGenerateDTO) {
        log.info("用户生成文案接口: {}", textGenerateDTO);
        TextGenerateVO textGenerateVO = textService.generateText(textGenerateDTO);
        return Result.success(textGenerateVO);
    }

}
