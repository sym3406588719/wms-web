package com.wms.msc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wms.msc.mapper")
public class MscApplication {

    public static void main(String[] args) {
        SpringApplication.run(MscApplication.class, args);
    }

}
