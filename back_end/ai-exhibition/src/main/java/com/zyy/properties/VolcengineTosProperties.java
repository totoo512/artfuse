package com.zyy.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "zyy.volcengine-tos")
@Data
public class VolcengineTosProperties {

    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;

}
