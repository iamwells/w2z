package io.github.iamwells.w2zserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"io.github.iamwells.w2zserver.mapper"})
public class W2zServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(W2zServerApplication.class, args);
    }
}
