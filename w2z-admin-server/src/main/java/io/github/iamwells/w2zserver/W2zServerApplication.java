package io.github.iamwells.w2zserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.lang.JoseException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan(value = {"io.github.iamwells.w2zserver.mapper"})
@Slf4j
public class W2zServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(W2zServerApplication.class, args);
    }
}
