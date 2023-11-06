package io.github.iamwells.w2zserver.controller;


import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.Method;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public void handleException(WebRequest request, Exception ex) throws Exception {
        Class<ResponseEntityExceptionHandler> handlerClass = ResponseEntityExceptionHandler.class;
        Method handleException = handlerClass.getMethod("handleException", Exception.class, WebRequest.class);
        request.setAttribute("ex_msg", ex.getMessage(),RequestAttributes.SCOPE_REQUEST);
        handleException.invoke(handlerClass.getDeclaredConstructor().newInstance(), ex, request);
    }

}
