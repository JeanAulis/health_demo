package com.health;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类：项目启动入口
 * @author ghy
 */
@SpringBootApplication
@MapperScan(basePackages = "com.health.mapper")
@Slf4j
public class HealthApplication {
    public static void main(String[] args) {

        SpringApplication.run(HealthApplication.class, args);
        log.info("启动成功");
    }
}
