package io.github.iamwells.w2zserver.properties.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtPayloadProperties {
    /**
     * 发布者
     */
    @Value("${spring.application.name}")
    private String iss = "w2z";
    /**
     * 主题
     */
    private String sub = "w2z-jwt-token";
    /**
     * 接收者
     */
    private Set<String> aud = new HashSet<>(Set.of("w2z-admin-user"));
    /**
     * 持续时间，单位：分钟
     */
    private Float exp = (float) (60 * 24 * 15);
}
