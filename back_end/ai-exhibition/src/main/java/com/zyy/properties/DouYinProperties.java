package com.zyy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zyy.douyin")
@Data
public class DouYinProperties {

    private String appid; //小程序的appid
    private String secret; //小程序的秘钥

}
