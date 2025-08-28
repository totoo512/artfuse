package com.zyy;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;
import com.zyy.constant.JwtClaimsConstant;
import com.zyy.properties.JwtProperties;
import com.zyy.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TosTest {

    @Test
    public void test() {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, 1);
        System.out.println(JwtUtil.createJWT("14.103.244.167-ai_exhibition-GEPm7skLxNhXKx5M", 2592000000L, claims));
    }
}
