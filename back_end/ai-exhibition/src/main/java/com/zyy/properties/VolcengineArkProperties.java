package com.zyy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zyy.volcengine-ark")
@Data
public class VolcengineArkProperties {

    private String apiKey;
    private String baseUrl;
    private String model;

}
