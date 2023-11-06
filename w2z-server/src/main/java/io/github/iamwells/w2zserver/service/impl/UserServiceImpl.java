package io.github.iamwells.w2zserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.iamwells.w2zserver.domain.User;
import io.github.iamwells.w2zserver.service.UserService;
import io.github.iamwells.w2zserver.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author iamwu
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-11-07 01:11:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




