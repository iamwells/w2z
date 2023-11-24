package io.github.iamwells.w2zserver;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"io.github.iamwells.w2zserver.mapper"})
@Slf4j
public class W2zAdminServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(W2zAdminServerApplication.class, args);
    }
}
