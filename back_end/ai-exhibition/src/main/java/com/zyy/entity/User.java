package com.zyy.entity;

import java.io.Serializable;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 用户基础信息表
* @TableName user
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    /**
     * 用户唯一标识
     */
    @ApiModelProperty("用户唯一标识")
    private Long userId;
    /**
     * 用户名（可选）
     */
    @ApiModelProperty("用户名（可选）")
    private String username;
    /**
     * 用户密码
     */
    @ApiModelProperty("用户密码")
    private String password;
    /**
     * 用户头像URL
     */
    @ApiModelProperty("用户头像URL")
    private String avatarUrl;
    /**
     * 手机号（用于登录/通知）
     */
    @ApiModelProperty("手机号（用于登录/通知）")
    private String phone;
    /**
     * 第三方平台唯一标识（抖音）
     */
    @ApiModelProperty("第三方平台唯一标识（抖音）")
    private String openid;
    /**
     * 已生成作品数
     */
    @ApiModelProperty("已生成作品数")
    private Integer generatedWorksCount;
    /**
     * 用户积分
     */
    @ApiModelProperty("用户积分")
    private Integer points;
    /**
     *
     */
    @ApiModelProperty("会员状态")
    private Integer membershipStatus;
    /**
     * 注册时间
     */
    @ApiModelProperty("注册时间")
    private LocalDateTime createTime;
    /**
     * 信息更新时间
     */
    @ApiModelProperty("信息更新时间")
    private LocalDateTime updateTime;

}
