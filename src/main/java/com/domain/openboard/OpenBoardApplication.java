package com.domain.openboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OpenBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenBoardApplication.class, args);
    }
}
