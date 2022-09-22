package com.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RewardsSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RewardsSystemApplication.class, args);
    }

}
