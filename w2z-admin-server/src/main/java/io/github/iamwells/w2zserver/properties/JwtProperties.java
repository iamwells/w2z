package io.github.iamwells.w2zserver.properties;


import io.github.iamwells.w2zserver.properties.jwt.JwtHeaderProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtPayloadProperties;
import io.github.iamwells.w2zserver.properties.jwt.JwtSignatureProperties;

public record JwtProperties(JwtHeaderProperties header, JwtPayloadProperties payload,
                            JwtSignatureProperties signature) {

}
