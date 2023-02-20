package org.boson.service;

import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.dto.MenuDto;
import org.boson.domain.dto.UserMenuDto;
import org.boson.domain.po.Menu;
import org.boson.domain.vo.ConditionVo;
import org.boson.domain.vo.MenuVo;
import org.boson.support.mybatisplus.service.LambdaCallable;

import java.util.List;

/**
 * 菜单服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface MenuService extends LambdaCallable<Menu> {

    /**
     * 新增或修改菜单
     *
     * @param menuVo 菜单信息
     */
    boolean saveOrUpdateMenu(MenuVo menuVo);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    boolean deleteMenu(Integer menuId);

    /**
     * 查看菜单列表
     *
     * @param conditionVo 查询条件
     * @return 菜单列表
     */
    List<MenuDto> listMenus(ConditionVo conditionVo);

    /**
     * 查看当前用户菜单
     *
     * @return 当前用户菜单列表
     */
    List<UserMenuDto> listUserMenus();

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项列表
     */
    List<LabelOptionDto> listMenuOptions();
}
