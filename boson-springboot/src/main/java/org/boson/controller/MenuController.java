package org.boson.controller;

import org.boson.annotation.LogOperation;
import org.boson.domain.Result;
import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.dto.MenuDto;
import org.boson.domain.dto.UserMenuDto;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.boson.enums.OperationEnum;
import org.boson.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 菜单控制器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Api(tags = "菜单模块")
@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 保存或修改菜单
     *
     * @param menuVo 菜单信息
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.SaveOrUpdate)
    @ApiOperation(value = "保存或修改菜单")
    @PostMapping("/admin/menus")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVo menuVo) {
        return Result.check(this.menuService.saveOrUpdateMenu(menuVo));
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return {@link Result<>}
     */
    @LogOperation(OperationEnum.Delete)
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/admin/menus/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        return Result.check(this.menuService.deleteMenu(menuId));
    }

    /**
     * 查询菜单列表
     *
     * @param conditionVo 查询条件
     * @return {@link Result<MenuDto>} 菜单列表
     */
    @ApiOperation(value = "查询菜单列表")
    @GetMapping("/admin/menus")
    public Result<List<MenuDto>> listMenus(ConditionVo conditionVo) {
        return Result.ok(this.menuService.listMenus(conditionVo));
    }

    /**
     * 查看当前用户菜单
     *
     * @return {@link Result<UserMenuDto>} 当前用户菜单列表
     */
    @ApiOperation(value = "查看当前用户菜单")
    @GetMapping("/admin/user/menus")
    public Result<List<UserMenuDto>> listUserMenus() {
        return Result.ok(this.menuService.listUserMenus());
    }

    /**
     * 查看角色菜单选项
     *
     * @return {@link Result<LabelOptionDto>} 角色菜单选项列表
     */
    @ApiOperation(value = "查看角色菜单选项")
    @GetMapping("/admin/role/menus")
    public Result<List<LabelOptionDto>> listMenuOptions() {
        return Result.ok(this.menuService.listMenuOptions());
    }
}
