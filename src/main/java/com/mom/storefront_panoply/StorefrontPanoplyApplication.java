package com.mom.storefront_panoply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class StorefrontPanoplyApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorefrontPanoplyApplication.class, args);
    }

}
