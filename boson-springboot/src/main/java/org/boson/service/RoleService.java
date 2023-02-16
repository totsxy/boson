package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.po.Role;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.RoleVo;
import org.boson.support.mybatisplus.service.Queryable;

import java.util.List;

/**
 * 角色服务
 *
 * @author yezhiqiu
 * @date 2021/08/10
 */
public interface RoleService extends Queryable<Role> {

    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    List<UserRoleDto> listUserRoles();

    /**
     * 获取资源角色选项
     * @return 资源
     */
    List<ResourceRoleDto> listResourceRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return 角色列表
     */
    PageResult<RoleDto> listRoles(ConditionVo conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVo roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);

}
