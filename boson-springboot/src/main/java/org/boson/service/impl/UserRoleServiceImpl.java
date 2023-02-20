package org.boson.service.impl;

import org.boson.domain.po.UserRole;
import org.boson.mapper.UserRoleMapper;
import org.boson.service.UserRoleService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 用户角色服务
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
