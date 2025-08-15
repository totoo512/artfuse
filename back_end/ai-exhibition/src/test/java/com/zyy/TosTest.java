package com.zyy;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;
import org.junit.jupiter.api.Test;

import java.io.*;

public class TosTest {

    @Test
    public void test() throws FileNotFoundException {
        String region = "cn-shanghai";
        String endpoint = "tos-cn-shanghai.volces.com";
        String accessKey = "AKLTOWNlYzFhNjBlNWMxNGM5Y2FjZDhlYTc3ZDk4OTZkYTE";
        String secretKey = "Wm1SbE1HSTBaak0wT0RnM05HUTBaRGs0TkdGaFlqUmpPREU1WVdFeVlUTQ==";
        String bucket = "douyin-ai";
        // 创建TOSClient实例。
        TOSV2 client = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        InputStream stream = new FileInputStream("C:\\Users\\32192\\Pictures\\微信图片_20250813181430.jpg");

        try{
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucket).setKey("object-crud-"+System.currentTimeMillis());
            PutObjectInput input = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
            PutObjectOutput output = client.putObject(input);
            System.out.println("Put object success, the object's etag is " + output.getEtag());
        } catch (TosException e) {
            System.out.println("Put object failed");
            e.printStackTrace();
        }
    }
}
