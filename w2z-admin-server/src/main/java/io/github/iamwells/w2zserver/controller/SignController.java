package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.domain.validate.group.Insert;
import io.github.iamwells.w2zserver.domain.validate.group.SignIn;
import io.github.iamwells.w2zserver.domain.validate.group.SignUp;
import io.github.iamwells.w2zserver.properties.TokenProperties;
import io.github.iamwells.w2zserver.service.SignService;
import io.github.iamwells.w2zserver.util.CommonEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.groups.Default;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;


    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping("/in")
    @SuppressWarnings("unchecked")
    public CommonEntity<Object> signIn(@RequestBody @Validated(value = {Default.class, SignIn.class}) User user, @RequestParam(defaultValue = "false") Boolean rememberMe, HttpServletResponse response, TokenProperties properties) {
        CommonEntity<Object> rs = signService.signIn(user);
        if (!rememberMe) {
            Object data = rs.getData();
            Collection<String> values = ((Map<String, String>) data).values();
            String[] tokens = values.toArray(new String[0]);
            Cookie cookie = new Cookie(properties.getName(), tokens[0]);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            rs.setData(null);
        }
        return rs;
    }

    @GetMapping("/out")
    public CommonEntity<Object> signOut(HttpServletRequest request, TokenProperties tokenProperties) {
        String token = request.getHeader(tokenProperties.getName());
        Long expire = (Long) request.getAttribute("tokenExpirationTime");
        return signService.signOut(token, expire);
    }

    @PostMapping("/up")
    public CommonEntity<Object> singUp(@RequestBody @Validated(value = {Default.class, Insert.class, SignUp.class}) User user) {
        return signService.signUp(user);
    }

    @GetMapping("/status")
    public CommonEntity<Object> status() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String status = "未登录";
        boolean isSingIn = false;
        if (authentication != null) {
            if (authentication.isAuthenticated()) {
                status = "已登录";
                isSingIn = true;
            }
        }
        return CommonEntity.ok(status, Map.of("isSignIn", isSingIn));
    }
}
