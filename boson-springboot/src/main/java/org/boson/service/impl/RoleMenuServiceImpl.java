package org.boson.service.impl;

import org.boson.mapper.RoleMenuMapper;
import org.boson.domain.po.RoleMenu;
import org.boson.service.RoleMenuService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 角色菜单服务
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
