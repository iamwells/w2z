package io.github.iamwells.w2zserver.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.properties.JwtProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtSignatureProperties;
import jakarta.annotation.PostConstruct;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import javax.naming.OperationNotSupportedException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;


public class JwtUtil {

    private static JwtProperties properties;
    private static Argon2PasswordEncoder secretEncoder;
    private static ObjectMapper claimsMapper;
    private static HmacKey hmacKey;
    private final JwtProperties jwtProperties;
    private final Argon2PasswordEncoder argon2PasswordEncoder;
    private final ObjectMapper objectMapper;


    @Autowired
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
            JwtPayloadProperties payload = getDefaultPayload(new JwtPayloadProperties(), user);
            JwtSignatureProperties signature = new JwtSignatureProperties();
            properties = new JwtProperties(header, payload, signature);
        } else if (properties.payload() != null) {
            setCertainAud(properties.payload(), user);
        }

        if (secretEncoder == null) {
            try {
                Class<?> argon2 = Class.forName("org.springframework.security.crypto.argon2");
                secretEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }

        if (claimsMapper == null) {
            try {
                Class<?> objectMapperClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                claimsMapper = new ObjectMapper();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            claimsMapper = new ObjectMapper();
        }
    }

    private static JwtPayloadProperties getDefaultPayload(JwtPayloadProperties payload, User user) {
        if (payload == null) {
            payload = new JwtPayloadProperties();
        }
        setCertainAud(payload, user);
        return payload;
    }

    private static void setCertainAud(JwtPayloadProperties payload, User user) {
        Set<String> audiences = payload.getAud();
        if (user != null) {
            String aud = user.getUsername();
            if (aud == null || aud.isEmpty()) {
                aud = user.getNickname();
            }
            if (aud != null && !aud.isEmpty()) {
                audiences.add(aud);
            }
        }
        payload.setAud(audiences);
    }


    public static String create(User user) {
        return create(properties, user);
    }

    private static String create(JwtProperties properties, User user) {
        checkAndInjectDependencies(properties, user);

        JwtHeaderProperties header = properties.header();
        JwtPayloadProperties payload = properties.payload();
        JwtSignatureProperties signature = properties.signature();

        JsonWebSignature jws = new JsonWebSignature();

        if (secretEncoder != null) {
            jws.setAlgorithmHeaderValue(header.getAlg());
            byte[] secret = secretEncoder.encode(signature.getSecret()).getBytes(StandardCharsets.UTF_8);
            hmacKey = new HmacKey(secret);
            try (
                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("jwt-admin.key"))
            ) {
                oos.writeObject(hmacKey);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            jws.setKey(hmacKey);
        }

        JwtClaims jwtClaims = new JwtClaims();
        try {
            jwtClaims.setGeneratedJwtId();
            jwtClaims.setIssuer(payload.getIss());
            jwtClaims.setIssuedAt(NumericDate.now());
            jwtClaims.setAudience(payload.getAud().stream().toList());
            jwtClaims.setSubject(payload.getSub());
            jwtClaims.setExpirationTimeMinutesInTheFuture(payload.getExp());
            jwtClaims.setClaim("user", user);
            String claimsJson;
            if (claimsMapper != null) {
                Map<String, Object> claimsMap = jwtClaims.getClaimsMap();
                claimsMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                claimsJson = claimsMapper.writeValueAsString(claimsMap);
            } else {
                claimsJson = jwtClaims.toJson();
            }
            jws.setPayload(claimsJson);
            return jws.getCompactSerialization();
        } catch (JsonProcessingException | JoseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setSecretEncoder(Argon2PasswordEncoder encoder) {
        if (secretEncoder == null) {
            secretEncoder = encoder;
        } else {
            throw new RuntimeException(new OperationNotSupportedException("密钥加密器禁止重复注入"));
        }
    }

    public static User verifyAndGetUser(String jwt) throws InvalidJwtException {
        return verifyAndGetUser(jwt, properties);
    }


    public static User verifyAndGetUser(String jwt, JwtProperties properties) {
        JwtClaims jwtClaims = verifyAndGetClaims(jwt, properties);
        Object userObj = jwtClaims.getClaimValue("user");
        User user = null;
        try {
            String userString = claimsMapper.writeValueAsString(userObj);
            user = claimsMapper.readValue(userString, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static JwtClaims verifyAndGetClaims(String jwt) throws InvalidJwtException {
        return verifyAndGetClaims(jwt, properties);
    }

    public static JwtClaims verifyAndGetClaims(String jwt, JwtProperties properties) {
        JwtConsumerBuilder jwtConsumerBuilder = new JwtConsumerBuilder()
                .setRequireJwtId()
                .setRequireExpirationTime()
                .setRequireIssuedAt()
                .setRequireSubject();
        checkAndInjectDependencies(properties, null);
        JwtPayloadProperties payload = properties.payload();
        if (payload != null) {
            Set<String> audiences = payload.getAud();
            String iss = payload.getIss();
            jwtConsumerBuilder
                    .setExpectedIssuer(iss)
                    .setExpectedAudience(audiences.toArray(new String[0]));
        }
        if (hmacKey == null) {
            try (
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("jwt.key"));
            ) {
                hmacKey = (HmacKey) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        jwtConsumerBuilder.setVerificationKey(hmacKey);
        JwtConsumer jwtConsumer = jwtConsumerBuilder.build();
        JwtContext process = null;
        try {
            process = jwtConsumer.process(jwt);
        } catch (InvalidJwtException e) {
            throw new RuntimeException(e);
        }
        return process.getJwtClaims();
    }
}
