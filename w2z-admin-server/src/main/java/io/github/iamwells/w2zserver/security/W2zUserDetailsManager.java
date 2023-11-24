package io.github.iamwells.w2zserver.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class W2zUserDetailsManager implements UserDetailsService {

    private final UserService userService;

    public W2zUserDetailsManager(UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException("没有找到该用户");
        }
        return user;
    }
}
