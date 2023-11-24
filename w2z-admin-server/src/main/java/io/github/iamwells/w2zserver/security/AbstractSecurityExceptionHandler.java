package io.github.iamwells.w2zserver.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.iamwells.w2zserver.util.CommonEntity;
import io.github.iamwells.w2zserver.util.ExceptionUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;


@Component
public abstract class AbstractSecurityExceptionHandler {

    private ObjectMapper objectMapper;

    public AbstractSecurityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    void doResponse(HttpServletResponse response, int status, Charset charset, MediaType mediaType, Exception exception) {
        response.setStatus(status);
        response.setCharacterEncoding(charset.toString());
        response.setContentType(mediaType.toString());
        String message = ExceptionUtil.deepCauseMessage(exception);

        try (PrintWriter writer = response.getWriter()) {
            CommonEntity<Object> error = CommonEntity.error(HttpStatus.valueOf(HttpServletResponse.SC_UNAUTHORIZED), null, message);
            if (objectMapper == null) {
                Class<?> objectMapperClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
                objectMapper = new ObjectMapper();
            }
            String result = objectMapper.writeValueAsString(error);
            writer.write(result);
            writer.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void doResponse(HttpServletResponse response, HttpStatus status, Charset charset, MediaType mediaType, Exception exception) {
        doResponse(response, status.value(), charset, mediaType, exception);
    }
}
