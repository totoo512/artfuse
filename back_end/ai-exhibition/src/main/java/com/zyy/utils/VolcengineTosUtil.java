package com.zyy.utils;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Data
@AllArgsConstructor
@Slf4j
public class VolcengineTosUtil {

    private String region;
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */
    public String upload(byte[] bytes, String objectName) {

        // 创建TOSClient实例。
        TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        InputStream stream = new ByteArrayInputStream(bytes);

        try{
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucket).setKey(objectName);
            PutObjectInput input = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
            PutObjectOutput output = client.putObject(input);
            System.out.println("Put object success, the object's etag is " + output.getEtag());
        } catch (TosException e) {
            System.out.println("Put object failed");
            e.printStackTrace();
        }

        //文件访问路径规则 https://douyin-ai.tos-cn-shanghai.volces.com/filename
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucket)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("文件上传到:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}
