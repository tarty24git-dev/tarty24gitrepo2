package com.cth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CthApplication {
    public static void main(String[] args) {
        SpringApplication.run(CthApplication.class, args);
    }
}
