package io.github.iamwells.w2zserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.util.JwtUtil;
import org.jose4j.lang.JoseException;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@MapperScan(value = {"io.github.iamwells.w2zserver.mapper"})
public class W2zServerApplication {
    public static void main(String[] args) throws JoseException, NoSuchAlgorithmException, JsonProcessingException {
        SpringApplication.run(W2zServerApplication.class, args);
        User user = new User();
        user.setId(1);
        user.setUsername("iamwells");
        user.setNickname("iamwells");
        user.setEmail("iamwells@163.com");
        user.setGender("男");
        String jwtToken = JwtUtil.createJwtToken(user);


        System.out.println(jwtToken);
    }
}
