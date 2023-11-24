package io.github.iamwells.w2zserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthenticationExceptionHandler extends AbstractSecurityExceptionHandler implements AuthenticationEntryPoint {
    public AuthenticationExceptionHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        doResponse(response, HttpStatus.UNAUTHORIZED, StandardCharsets.UTF_8, MediaType.APPLICATION_JSON,authException);
    }
}
