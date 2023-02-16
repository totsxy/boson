package org.boson.service;

import org.boson.domain.dto.LabelOptionDTO;
import org.boson.domain.dto.MenuDTO;
import org.boson.domain.dto.UserMenuDTO;
import org.boson.domain.po.Menu;
import org.boson.domain.vo.ConditionVO;
import org.boson.domain.vo.MenuVO;
import org.boson.support.mybatisplus.service.Queryable;

import java.util.List;

/**
 * 菜单服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
public interface MenuService extends Queryable<Menu> {

    /**
     * 查看菜单列表
     *
     * @param conditionVO 查询条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    Boolean saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    Boolean deleteMenu(Integer menuId);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

}
