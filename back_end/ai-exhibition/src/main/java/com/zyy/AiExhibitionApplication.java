package com.zyy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.zyy.mapper")
@EnableTransactionManagement
public class AiExhibitionApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiExhibitionApplication.class, args);
    }

}
