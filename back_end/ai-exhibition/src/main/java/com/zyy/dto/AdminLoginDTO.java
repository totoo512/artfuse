package com.zyy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 管理端登录DTO
 */
@Data
@ApiModel(description = "管理端登录DTO")
public class AdminLoginDTO implements Serializable {

    /**
     * 登录验证码
     */
    @ApiModelProperty(value = "登录验证码", required = true)
    @NotBlank(message = "登录验证码不能为空")
    private String code;

}