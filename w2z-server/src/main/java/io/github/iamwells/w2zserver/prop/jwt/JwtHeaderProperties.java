package io.github.iamwells.w2zserver.prop.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jose4j.jws.AlgorithmIdentifiers;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtHeaderProperties {
    /**
     * 加密算法
     */
    private String alg = AlgorithmIdentifiers.HMAC_SHA256;

    /**
     * 类型
     */
    private String typ = "JWT";
}