package org.boson.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.constant.CommonConst;
import org.boson.domain.PageResult;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.po.UserRole;
import org.boson.domain.vo.ConditionVo;
import org.boson.mapper.RoleMapper;
import org.boson.mapper.UserRoleMapper;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.po.Role;
import org.boson.domain.po.RoleMenu;
import org.boson.domain.po.RoleResource;
import org.boson.exception.BizException;
import org.boson.handler.ResourceRoleMetadataSourceImpl;
import org.boson.service.RoleMenuService;
import org.boson.service.RoleResourceService;
import org.boson.service.RoleService;
import org.boson.domain.vo.RoleVo;
import org.boson.service.UserRoleService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.boson.util.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色服务
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ResourceRoleMetadataSourceImpl filterInvocationSecurityMetadataSource;

    @Override
    public List<UserRoleDto> listUserRoles() {
        // 查询角色列表
//        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
//                .select(Role::getId, Role::getRoleName));
//        return BeanCopyUtils.copyList(roleList, UserRoleDto.class);

        return this.beginQuery()
                .select(Role::getId, Role::getRoleName)
                .queryListAndPo2Vo(UserRoleDto.class);
    }

    @Override
    public List<ResourceRoleDto> listResourceRoles() {
        return this.getBaseMapper().listResourceRoles();
    }

    @Override
    public PageResult<RoleDto> listRoles(ConditionVo conditionVO) {
        // 查询角色列表
        List<RoleDto> roleDtoList = getBaseMapper().listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        // 查询总量
//        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
//                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords()));
//
        int count = this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Role::getRoleName, conditionVO.getKeywords())
                .count();

        return new PageResult<>(roleDtoList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveOrUpdateRole(RoleVo roleVO) {
        // 判断角色名重复
//        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
//                .select(Role::getId)
//                .eq(Role::getRoleName, roleVO.getRoleName()));

        Role existRole = this.beginQuery()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName())
                .getOne();

        if (Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId())) {
            throw new BizException("角色名已存在");
        }

        // 保存或更新角色信息
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(CommonConst.FALSE)
                .build();

        this.saveOrUpdate(role);

        // 更新角色资源关系
        if (Objects.nonNull(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }

            List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId)
                            .build())
                    .collect(Collectors.toList());

            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            filterInvocationSecurityMetadataSource.clearDataSource(true);
        }

        // 更新角色菜单关系
        if (Objects.nonNull(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleVO.getId()));
            }

            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId)
                            .build())
                    .collect(Collectors.toList());

            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断角色下是否有用户
//        Integer count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
//                .in(UserRole::getRoleId, roleIdList));

        int count = userRoleService.beginQuery()
                .in(UserRole::getRoleId, roleIdList)
                .count();

        if (count > 0) {
            throw new BizException("该角色下存在用户");
        }

        getBaseMapper().deleteBatchIds(roleIdList);
    }

}
