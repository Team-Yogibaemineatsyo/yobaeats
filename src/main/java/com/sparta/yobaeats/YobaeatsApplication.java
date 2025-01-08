package com.sparta.yobaeats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class YobaeatsApplication {

    public static void main(String[] args) {
        SpringApplication.run(YobaeatsApplication.class, args);
    }

}
