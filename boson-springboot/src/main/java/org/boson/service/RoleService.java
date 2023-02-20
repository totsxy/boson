package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.po.Role;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.RoleVo;
import org.boson.support.mybatisplus.service.LambdaCallable;

import java.util.List;

/**
 * 角色服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface RoleService extends LambdaCallable<Role> {

    /**
     * 保存或更新角色
     *
     * @param roleVo 角色
     */
    boolean saveOrUpdateRole(RoleVo roleVo);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    boolean deleteRoles(List<Integer> roleIdList);

    /**
     * 查询角色列表
     *
     * @param conditionVo 查询条件
     * @return 角色列表
     */
    PageResult<RoleDto> listRoles(ConditionVo conditionVo);

    /**
     * 获取用户角色选项
     *
     * @return 用户角色选项列表
     */
    List<UserRoleDto> listUserRoles();

    /**
     * 获取资源角色选项
     *
     * @return 资源角色选项列表
     */
    List<ResourceRoleDto> listResourceRoles();
}
