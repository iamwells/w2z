package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.util.CommonEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public CommonEntity<Object> error(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        if (statusCode == null) {
            throw new RuntimeException("由于没有发生什么错误所以没有发生什么错误呢(*^_^*)");
        }
        String message = (String) request.getAttribute("ex_msg");
        return CommonEntity.error(HttpStatus.valueOf(statusCode), null, message);
    }
}
