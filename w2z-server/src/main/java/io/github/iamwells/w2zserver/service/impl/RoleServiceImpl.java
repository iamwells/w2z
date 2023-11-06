package io.github.iamwells.w2zserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.iamwells.w2zserver.domain.Role;
import io.github.iamwells.w2zserver.service.RoleService;
import io.github.iamwells.w2zserver.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author iamwu
* @description 针对表【role(角色)】的数据库操作Service实现
* @createDate 2023-11-07 01:27:43
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




