package org.boson.service;

import org.boson.domain.po.UserRole;
import org.boson.support.service.BaseService;


/**
 * 用户角色服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface UserRoleService extends BaseService<UserRole> {

    /**
     * 按用户信息id删除用户角色
     *
     * @param userInfoId 用户信息id
     */
    boolean removeByUserInfoId(Integer userInfoId);
}
