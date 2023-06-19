package org.boson.service.impl;

import org.boson.domain.po.UserRole;
import org.boson.mapper.UserRoleMapper;
import org.boson.service.UserRoleService;
import org.boson.support.service.BaseServiceImpl;
import org.springframework.stereotype.Service;


/**
 * 用户角色服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class UserRoleServiceImpl extends BaseServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public boolean removeByUserInfoId(Integer userInfoId) {
        return this.beginQuery()
                .eq(UserRole::getUserId, userInfoId)
                .remove();
    }
}
