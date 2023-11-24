package io.github.iamwells.w2zserver.prop;


import io.github.iamwells.w2zserver.prop.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.prop.jwt.JwtSignatureProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

public record JwtProperties(JwtHeaderProperties header, JwtPayloadProperties payload,
                            JwtSignatureProperties signature) {

}
