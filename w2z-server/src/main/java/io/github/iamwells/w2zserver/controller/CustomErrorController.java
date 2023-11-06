package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.util.CommonEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("${server.error.path:${error.path:/error}}")
    public CommonEntity<Object> error(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message = (String) request.getAttribute("ex_msg");
        return CommonEntity.error(HttpStatus.valueOf(statusCode), null, message);
    }
}
