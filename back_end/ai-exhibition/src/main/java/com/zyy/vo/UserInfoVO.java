package com.zyy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息响应VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户信息响应VO")
public class UserInfoVO {

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户头像地址
     */
    @ApiModelProperty("用户头像地址")
    private String avatarUrl;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 已生成作品数量
     */
    @ApiModelProperty("已生成作品数量")
    private Integer generatedWorksCount;

    /**
     * 用户积分
     */
    @ApiModelProperty("用户积分")
    private Integer points;

    /**
     * 会员状态：0=普通会员，1=高级会员
     */
    @ApiModelProperty("会员状态：0=普通会员，1=高级会员")
    private Integer membershipStatus;
}