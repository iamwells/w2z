package io.github.iamwells.w2zserver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamwells.w2zserver.config.JwtConfiguration;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.prop.JwtProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtSignatureProperties;
import jakarta.annotation.PostConstruct;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.naming.OperationNotSupportedException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@ConditionalOnBean({JwtConfiguration.class})
@Component
public class JwtUtil {

    private static JwtProperties properties;
    private static Argon2PasswordEncoder secretEncoder;
    private static ObjectMapper claimsMapper;
    private final JwtProperties jwtProperties;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final ObjectMapper objectMapper;


    public JwtUtil(JwtProperties jwtProperties, Argon2PasswordEncoder argon2PasswordEncoder, ObjectMapper objectMapper) {
        this.jwtProperties = jwtProperties;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void post() {
        properties = jwtProperties;
        secretEncoder = argon2PasswordEncoder;
        claimsMapper = objectMapper;
    }

    private static void checkAndInjectDependencies(JwtProperties properties, User user) {
        if (properties == null) {
            JwtHeaderProperties header = new JwtHeaderProperties();
            header.setAlg(AlgorithmIdentifiers.HMAC_SHA256);
            header.setTyp("JWT");
            JwtPayloadProperties payload = getDefaultPayload(user);
            JwtSignatureProperties signature = new JwtSignatureProperties();
            signature.setSecret("w2z-server");
            properties = new JwtProperties(header, payload, signature);
        } else if (properties.payload() != null) {
            properties.payload().setAud(getCertainAud(user));
        }

        if (secretEncoder == null) {
            secretEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
        }

        if (claimsMapper == null) {
            claimsMapper = new ObjectMapper();
        }
    }

    private static JwtPayloadProperties getDefaultPayload(User user) {
        JwtPayloadProperties payload = new JwtPayloadProperties();
        payload.setIss("w2z-server");
        payload.setSub("w2z-jwt-token");
        String aud = getCertainAud(user);
        payload.setAud(aud);
        payload.setExp((float) (60 * 24 * 15));
        return payload;
    }

    private static String getCertainAud(User user) {
        String aud = user.getUsername();
        if (aud == null || aud.isEmpty()) {
            Integer id = user.getId();
            if (id != null) {
                aud = id.toString();
            }
        }
        if (aud == null || aud.isEmpty()) {
            String nickname = user.getNickname();
            aud = nickname == null ? "blog user" : nickname;
        }
        return aud;
    }


    public static String createJwtToken(User user) throws JoseException, JsonProcessingException {
        return createJwtToken(properties, user);
    }

    private static String createJwtToken(JwtProperties properties, User user) throws JsonProcessingException, JoseException {
        checkAndInjectDependencies(properties, user);

        JwtHeaderProperties header = properties.header();
        JwtPayloadProperties payload = properties.payload();
        JwtSignatureProperties signature = properties.signature();

        JsonWebSignature jws = new JsonWebSignature();
        jws.setContentTypeHeaderValue(header.getTyp());

        if (secretEncoder != null) {
            jws.setAlgorithmHeaderValue(header.getAlg());
            byte[] secret = secretEncoder.encode(signature.getSecret()).getBytes(StandardCharsets.UTF_8);
            jws.setKey(new HmacKey(secret));
        }

        JwtClaims jwtClaims = new JwtClaims();
        jwtClaims.setGeneratedJwtId();
        jwtClaims.setIssuer(payload.getIss());
        jwtClaims.setIssuedAt(NumericDate.now());
        jwtClaims.setAudience(payload.getAud());
        jwtClaims.setSubject(payload.getSub());
        jwtClaims.setExpirationTimeMinutesInTheFuture(payload.getExp());
        jwtClaims.setClaim("user", user);
        Map<String, Object> claimsMap = jwtClaims.getClaimsMap();
        claimsMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String claimsJson = claimsMapper.writeValueAsString(claimsMap);
        jws.setPayload(claimsJson);

        return jws.getCompactSerialization();
    }

    public static void setSecretEncoder(Argon2PasswordEncoder encoder) throws OperationNotSupportedException {
        if (secretEncoder == null) {
            secretEncoder = encoder;
        } else {
            throw new OperationNotSupportedException("密钥加密器禁止重复注入");
        }
    }
}
