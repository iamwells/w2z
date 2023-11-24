package io.github.iamwells.w2zserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.mapper.UserMapper;
import io.github.iamwells.w2zserver.service.SignService;
import io.github.iamwells.w2zserver.util.CommonEntity;
import io.github.iamwells.w2zserver.util.JwtUtil;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.naming.OperationNotSupportedException;
import java.sql.SQLException;
import java.util.Map;

@Service
public class SignServiceImpl extends ServiceImpl<UserMapper, User> implements SignService {


    private final RedissonClient redissonClient;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public SignServiceImpl(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, RedissonClient redissonClient) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.redissonClient = redissonClient;
    }


    @Override
    public CommonEntity<Object> signIn(User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String jwtToken = null;
        if (username != null && password != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);
            if (authenticate.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                Object userInfo = authenticate.getPrincipal();
                if (userInfo instanceof User) {
                    ((User) userInfo).setPassword(null);
                    jwtToken = JwtUtil.create((User) userInfo);
                }
            }
        }
        if (!StringUtils.hasText(jwtToken)) {
            throw new RuntimeException(new InvalidJwtException("生成jwt失败", null, null));
        }
        return CommonEntity.ok("登陆成功", Map.of("token", jwtToken));
    }

    @Override
    public CommonEntity<Object> signOut(String token, Long expire) {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        RScoredSortedSet<Object> tokenBlackList = redissonClient.getScoredSortedSet("tokenBlackList");
        if (!tokenBlackList.contains(token)) {
            tokenBlackList.removeRangeByScore(0, true, System.currentTimeMillis(), true);
            tokenBlackList.add(expire * 1.0, token);
        } else {
            throw new RuntimeException(new OperationNotSupportedException("请不要重复注销！"));
        }
        return CommonEntity.ok("注销用户成功", null);
    }

    @Override
    public CommonEntity<Object> signUp(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!save(user)) {
            throw new RuntimeException(new SQLException("注册用户失败，可能是用户名重复、缺少参数或其他原因"));
        }
        return CommonEntity.ok("注册用户成功", null);
    }
}
