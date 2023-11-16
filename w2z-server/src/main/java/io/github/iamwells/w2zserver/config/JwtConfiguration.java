package io.github.iamwells.w2zserver.config;


import io.github.iamwells.w2zserver.prop.JwtProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtSignatureProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
