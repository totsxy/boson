package org.boson.service;

import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.po.Role;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.RoleVo;
import org.boson.support.service.BaseService;

import java.util.List;


/**
 * 角色服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 保存或更新角色
     *
     * @param roleVo 角色信息
     */
    boolean saveOrUpdateRole(RoleVo roleVo);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    boolean deleteRoles(List<Integer> roleIdList);

    /**
     * 分页查询角色列表
     *
     * @param conditionVo 查询条件
     * @return 角色列表
     */
    PageResult<RoleDto> pageRoles(ConditionVo conditionVo);

    /**
     * 根据用户信息id查询角色列表
     *
     * @param userInfoId 用户信息id
     * @return 角色标签
     */
    List<String> listRolesByUserInfoId(Integer userInfoId);

    /**
     * 查看用户角色列表
     *
     * @return 用户角色列表
     */
    List<UserRoleDto> listUserRoles();

    /**
     * 查看资源角色列表
     *
     * @return 资源角色列表
     */
    List<ResourceRoleDto> listResourceRoles();
}
