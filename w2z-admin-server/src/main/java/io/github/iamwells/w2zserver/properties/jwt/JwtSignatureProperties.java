package io.github.iamwells.w2zserver.properties.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtSignatureProperties {
    /**
     * 密钥
     */
    private String secret = "w2z-secret";
}