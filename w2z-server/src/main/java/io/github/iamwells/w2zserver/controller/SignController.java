package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.service.LoginAndLogoutService;
import io.github.iamwells.w2zserver.util.CommonEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginAndLogoutController {

    private final LoginAndLogoutService loginAndLogoutService;

    public LoginAndLogoutController(LoginAndLogoutService loginAndLogoutService) {
        this.loginAndLogoutService = loginAndLogoutService;
    }


    @PostMapping("/login")
    public CommonEntity<Object> login(@RequestBody User user) {
        return loginAndLogoutService.login(user);
    }

    @PostMapping("/logout")
    public CommonEntity<Object> logout(User user) {

        return null;
    }

}
