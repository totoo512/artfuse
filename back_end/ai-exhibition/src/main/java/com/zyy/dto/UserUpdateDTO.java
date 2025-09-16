package com.zyy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 用户信息修改请求DTO
 */
@Data
public class UserUpdateDTO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 头像URL
     */
    @ApiModelProperty("头像URL")
    private String avatarUrl;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 生成作品数量
     */
    @ApiModelProperty("生成作品数量")
    private Integer generatedWorksCount;

    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Integer points;

    /**
     * 会员状态
     */
    @ApiModelProperty("会员状态")
    private Integer membershipStatus;
}