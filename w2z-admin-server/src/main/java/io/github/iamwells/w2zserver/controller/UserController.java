package io.github.iamwells.w2zserver.controller;


import io.github.iamwells.w2zserver.domain.UserWithWhere;
import io.github.iamwells.w2zserver.domain.validate.group.Insert;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.domain.validate.group.WithoutPwd;
import io.github.iamwells.w2zserver.service.UserService;
import io.github.iamwells.w2zserver.util.CommonEntity;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("")
    public CommonEntity<Object> add(@RequestBody @Validated({Default.class, Insert.class}) User user) {
        return userService.add(user);
    }

    @DeleteMapping("")
    public CommonEntity<Object> delete(@RequestBody @Validated User user) {
        return userService.removeByUser(user);
    }

    @PutMapping("")
    public CommonEntity<Object> update(@RequestBody @Validated({Default.class, WithoutPwd.class}) UserWithWhere user) {
        return userService.updateByUser(user);
    }

    @GetMapping("")
    public CommonEntity<User> get(@RequestBody @Validated User user) {
        return userService.getByUser(user);
    }
    @GetMapping
    public CommonEntity<Object> getList(@RequestBody(required = false) @Validated User user) {
        return null;
    }
}
