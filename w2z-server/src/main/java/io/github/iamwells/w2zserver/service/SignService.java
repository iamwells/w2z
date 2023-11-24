package io.github.iamwells.w2zserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.util.CommonEntity;

public interface LoginAndLogoutService extends IService<User> {
    CommonEntity<Object> login(User user);

    CommonEntity<Object> logout(User user);
}
