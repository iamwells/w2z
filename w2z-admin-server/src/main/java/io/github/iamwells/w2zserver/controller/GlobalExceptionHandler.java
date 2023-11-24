package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.util.CommonEntity;
import io.github.iamwells.w2zserver.util.ExceptionUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> handleHttpMessageConversionException(WebRequest request, Exception ex) throws Exception {
        String message = ExceptionUtil.deepCauseMessage(ex);
        request.setAttribute("ex_msg", message, RequestAttributes.SCOPE_REQUEST);
        HttpStatusCode httpStatusCode = HttpStatusCode.valueOf(500);
        HttpStatus httpStatus = HttpStatus.valueOf(500);
        CommonEntity<Object> body = CommonEntity.error(httpStatus, null, message);
        return super.handleExceptionInternal(ex, body, null, httpStatusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getMessage();
        int s = message.lastIndexOf("[");
        int e = message.lastIndexOf("]]");
        String msg = message.substring(s + 1, e);
        CommonEntity<Object> body = CommonEntity.error(status.value(), null, msg);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        if (body == null) {
            body = CommonEntity.error(statusCode.value(), null, ex.getMessage());
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGlobalException(WebRequest request, Exception ex) throws Exception {
        if (ex instanceof HttpMessageConversionException) {
            handleHttpMessageConversionException(request, ex);
        }
        String message = ExceptionUtil.deepCauseMessage(ex);
        request.setAttribute("ex_msg", message, RequestAttributes.SCOPE_REQUEST);
        return handleException(ex, request);
    }

}
