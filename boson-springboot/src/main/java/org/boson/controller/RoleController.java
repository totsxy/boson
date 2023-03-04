package org.boson.controller;

import org.boson.annotation.LogOperation;
import org.boson.domain.PageResult;
import org.boson.domain.Result;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.dto.UserRoleDto;
import org.boson.domain.vo.ConditionVo;
import org.boson.enums.OperationEnum;
import org.boson.service.RoleService;
import org.boson.domain.vo.RoleVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "角色模块")
@RestController
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 保存或更新角色
     *
     * @param roleVo 角色信息
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.SaveOrUpdate)
    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> saveOrUpdateRole(@RequestBody @Valid RoleVo roleVo) {
        return Result.check(this.roleService.saveOrUpdateRole(roleVo));
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.Remove)
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/admin/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        return Result.check(this.roleService.deleteRoles(roleIdList));
    }

    /**
     * 查询角色列表
     *
     * @param conditionVo 查询条件
     * @return {@link Result<RoleDto>} 角色列表
     */
    @ApiOperation(value = "查询角色列表")
    @GetMapping("/admin/roles")
    public Result<PageResult<RoleDto>> listRoles(ConditionVo conditionVo) {
        return Result.ok(this.roleService.listRoles(conditionVo));
    }

    /**
     * 查询用户角色选项
     *
     * @return {@link Result<UserRoleDto>} 用户角色选项列表
     */
    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/admin/users/role")
    public Result<List<UserRoleDto>> listUserRoles() {
        return Result.ok(this.roleService.listUserRoles());
    }
}
