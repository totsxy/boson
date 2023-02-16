package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDTO;
import org.boson.domain.dto.RoleDTO;
import org.boson.domain.dto.UserRoleDTO;
import org.boson.domain.po.Role;
import org.boson.domain.vo.ConditionVO;
import org.boson.domain.vo.RoleVO;
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
    List<UserRoleDTO> listUserRoles();

    /**
     * 获取资源角色选项
     * @return 资源
     */
    List<ResourceRoleDTO> listResourceRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return 角色列表
     */
    PageResult<RoleDTO> listRoles(ConditionVO conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);

}
