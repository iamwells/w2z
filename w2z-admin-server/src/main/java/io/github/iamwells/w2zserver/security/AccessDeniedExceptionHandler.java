package io.github.iamwells.w2zserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AccessDeniedExceptionHandler extends AbstractSecurityExceptionHandler implements AccessDeniedHandler {
    public AccessDeniedExceptionHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
        doResponse(response, HttpServletResponse.SC_FORBIDDEN, StandardCharsets.UTF_8, MediaType.APPLICATION_JSON,accessDeniedException);
    }
}
