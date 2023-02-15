package org.boson.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.boson.domain.po.UserRole;
import org.boson.mapper.UserRoleMapper;
import org.boson.service.UserRoleService;
import org.springframework.stereotype.Service;


/**
 * 用户角色服务
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


}
