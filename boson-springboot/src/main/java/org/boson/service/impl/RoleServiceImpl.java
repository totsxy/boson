package org.boson.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.po.UserRole;
import org.boson.domain.vo.ConditionVo;
import org.boson.mapper.RoleMapper;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.po.Role;
import org.boson.domain.po.RoleMenu;
import org.boson.domain.po.RoleResource;
import org.boson.exception.BizException;
import org.boson.handler.SecurityMetadataSourceImpl;
import org.boson.service.RoleMenuService;
import org.boson.service.RoleResourceService;
import org.boson.service.RoleService;
import org.boson.domain.vo.RoleVo;
import org.boson.service.UserRoleService;
import org.boson.support.service.BaseServiceImpl;
import org.boson.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.boson.constant.CommonConstants.FALSE;


/**
 * 角色服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private SecurityMetadataSourceImpl securityMetadataSource;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOrUpdateRole(RoleVo roleVo) {
        // 判断角色名重复
        Role existRole = this.beginQuery()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVo.getRoleName())
                .getOne();

        if (Objects.nonNull(existRole) && !existRole.getId().equals(roleVo.getId())) {
            throw new BizException("角色名已存在");
        }

        // 保存或更新角色信息
        Role role = Role.builder()
                .id(roleVo.getId())
                .roleName(roleVo.getRoleName())
                .roleLabel(roleVo.getRoleLabel())
                .isDisable(FALSE)
                .build();

        if (!this.saveOrUpdate(role)) {
            return false;
        }

        // 更新角色菜单关系
        if (Objects.nonNull(roleVo.getMenuIdList())) {
            if (Objects.nonNull(roleVo.getId())) {
                this.roleMenuService.beginQuery()
                        .eq(RoleMenu::getRoleId, roleVo.getId())
                        .remove();
            }

            List<RoleMenu> roleMenuList = roleVo.getMenuIdList()
                    .stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());

            if (!this.roleMenuService.saveBatch(roleMenuList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }
        }

        // 更新角色资源关系
        if (Objects.nonNull(roleVo.getResourceIdList())) {
            if (Objects.nonNull(roleVo.getId())) {
                this.roleResourceService.beginQuery()
                        .eq(RoleResource::getRoleId, roleVo.getId())
                        .remove();
            }

            List<RoleResource> roleResourceList = roleVo.getResourceIdList()
                    .stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());

            if (!this.roleResourceService.saveBatch(roleResourceList)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return false;
            }

            // refresh resource role.
            this.securityMetadataSource.loadResourceRole();
        }

        return true;
    }

    @Override
    public boolean deleteRoles(List<Integer> roleIdList) {
        int count = this.userRoleService.beginQuery()
                .in(UserRole::getRoleId, roleIdList)
                .count();

        if (count > 0) {
            throw new BizException("该角色下存在用户");
        }

        return this.removeByIds(roleIdList);
    }

    @Override
    public PageResult<RoleDto> pageRoles(ConditionVo conditionVo) {
        List<RoleDto> roleDtoList = this.getBaseMapper()
                .pageRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVo);

        int count = this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Role::getRoleName, conditionVo.getKeywords())
                .count();

        return PageResult.of(roleDtoList, count);
    }

    @Override
    public List<String> listRolesByUserInfoId(Integer userInfoId) {
        return this.getBaseMapper().listRolesByUserInfoId(userInfoId);
    }

    @Override
    public List<UserRoleDto> listUserRoles() {
        return this.beginQuery()
                .select(Role::getId, Role::getRoleName)
                .queryListAndPo2Vo(UserRoleDto.class);
    }

    @Override
    public List<ResourceRoleDto> listResourceRoles() {
        return this.getBaseMapper().listResourceRoles();
    }
}
