package com.yummy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //enable transaction
@Slf4j
public class YummyApplication {
    public static void main(String[] args) {
        SpringApplication.run(YummyApplication.class, args);
        log.info("server started");
    }
}
