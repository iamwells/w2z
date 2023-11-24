package io.github.iamwells.w2zserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.domain.UserWithWhere;
import io.github.iamwells.w2zserver.mapper.UserMapper;
import io.github.iamwells.w2zserver.service.UserService;
import io.github.iamwells.w2zserver.util.CommonEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.Map;

/**
* @author iamwu
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-11-07 01:11:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    private final Argon2PasswordEncoder passwordEncoder;

    public UserServiceImpl(Argon2PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CommonEntity<Object> add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!save(user)) {
            throw new RuntimeException(new SQLException("添加用户失败，可能是用户名重复、缺少参数或其他原因"));
        }
        return CommonEntity.ok("添加用户成功", null);
    }

    @Override
    public CommonEntity<Object> removeByUser(User user) {
        boolean removed;
        if (user.getId() != null) {
            removed = removeById(user.getId());
        } else if (StringUtils.hasText(user.getUsername())) {
            removed = removeByMap(Map.of("username", user.getUsername()));
        } else if (StringUtils.hasText(user.getEmail())) {
            removed = removeByMap(Map.of("email", user.getEmail()));
        } else if (StringUtils.hasText(user.getPhoneNumber())) {
            removed = removeByMap(Map.of("phone_number", user.getPhoneNumber()));
        } else {
            removed = remove(new QueryWrapper<>(user));
        }
        if (!removed) {
            throw new RuntimeException("删除失败，可能是如下原因：1.记录不存在或已删除；2.条件无法匹配到用户");
        }
        return CommonEntity.ok("删除成功", null);
    }

    @Override
    public CommonEntity<Object> updateByUser(UserWithWhere user) {
        boolean updated = update(user, new UpdateWrapper<>(user.getWhere()));
        if (!updated) {
            throw new RuntimeException(new SQLException("修改失败，可能是如下原因：1.记录未发生改变或不存在；2.条件无法匹配到用户"));
        }
        return CommonEntity.ok("修改成功", null);
    }

    @Override
    public CommonEntity<User> getByUser(User user) {
        User resultUser = null;
        if (user.getId() != null) {
            resultUser = getById(user.getId());
        } else if (StringUtils.hasText(user.getUsername())) {
            resultUser = getOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        } else if (StringUtils.hasText(user.getEmail())) {
            resultUser = getOne(new QueryWrapper<User>().eq("email", user.getEmail()));
        } else if (StringUtils.hasText(user.getPhoneNumber())) {
            resultUser = getOne(new QueryWrapper<User>().eq("phone_number", user.getPhoneNumber()));
        } else {
            resultUser = getOne(new QueryWrapper<>(user));
        }
        if (resultUser==null) {
            throw new RuntimeException("删除失败，可能是如下原因：1.记录不存在或已删除；2.条件无法匹配到用户");
        }
        return CommonEntity.ok("查询成功", resultUser);
    }
}




