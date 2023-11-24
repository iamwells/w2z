package io.github.iamwells.w2zserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.domain.UserWithWhere;
import io.github.iamwells.w2zserver.util.CommonEntity;

/**
* @author iamwu
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2023-11-07 01:11:35
*/
public interface UserService extends IService<User> {

    CommonEntity<Object> add(User user);

    CommonEntity<Object> removeByUser(User user);

    CommonEntity<Object> updateByUser(UserWithWhere user);

    CommonEntity<User> getByUser(User user);
}
