package io.github.iamwells.w2zserver.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamwells.w2zserver.filter.JwtTokenFilter;
import io.github.iamwells.w2zserver.properties.JwtProperties;
import io.github.iamwells.w2zserver.properties.TokenProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtSignatureProperties;
import io.github.iamwells.w2zserver.util.JwtUtil;
import org.apache.el.parser.Token;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Configuration
public class JwtConfiguration {

    @Bean
    @ConfigurationProperties("jwt.header")
    public JwtHeaderProperties header() {
        return new JwtHeaderProperties();
    }

    @Bean
    @ConfigurationProperties("jwt.payload")
    public JwtPayloadProperties payload() {
        return new JwtPayloadProperties();
    }

    @Bean
    @ConfigurationProperties("jwt.signature")
    public JwtSignatureProperties signature() {
        return new JwtSignatureProperties();
    }

    @Bean
    @ConditionalOnBean({JwtHeaderProperties.class, JwtPayloadProperties.class, JwtSignatureProperties.class})
    public JwtProperties jwtProperties(JwtHeaderProperties header, JwtPayloadProperties payload, JwtSignatureProperties signature) {
        return new JwtProperties(header, payload, signature);
    }

    @Bean
    @ConditionalOnBean({JwtConfiguration.class})
    public JwtUtil jwtUtil(JwtProperties jwtProperties, Argon2PasswordEncoder argon2PasswordEncoder, ObjectMapper objectMapper) {
        return new JwtUtil(jwtProperties, argon2PasswordEncoder, objectMapper);
    }

    @Bean
    @ConfigurationProperties("jwt.token")
    public TokenProperties tokenProperties() {
        return new TokenProperties();
    }
}
