package io.github.iamwells.w2zserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.util.CommonEntity;

import javax.naming.OperationNotSupportedException;

public interface SignService extends IService<User> {
    CommonEntity<Object> signIn(User user);

    CommonEntity<Object> signOut(String token, Long expire);

    CommonEntity<Object> signUp(User user);
}
