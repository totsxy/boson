package org.boson.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.dto.LabelOptionDto;
import org.boson.domain.vo.ConditionVo;
import org.boson.mapper.MenuMapper;
import org.boson.domain.dto.MenuDto;
import org.boson.domain.dto.UserMenuDto;
import org.boson.domain.po.Menu;
import org.boson.domain.po.RoleMenu;
import org.boson.exception.BizException;
import org.boson.domain.vo.MenuVo;
import org.boson.service.MenuService;
import org.boson.service.RoleMenuService;
import org.boson.support.service.BaseServiceImpl;
import org.boson.util.BeanUtils;
import org.boson.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.boson.constant.CommonConstants.*;

/**
 * 菜单服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public boolean saveOrUpdateMenu(MenuVo menuVo) {
        return this.saveOrUpdate(BeanUtils.bean2Bean(menuVo, Menu.class));
    }

    @Override
    public boolean deleteMenu(Integer menuId) {
        // 查询是否有角色关联
        int count = this.roleMenuService.beginQuery()
                .eq(RoleMenu::getMenuId, menuId)
                .count();

        if (count > 0) {
            throw new BizException("菜单下有角色关联");
        }

        // 查询子菜单
        List<Integer> menuIdList = this.beginQuery()
                .select(Menu::getId)
                .eq(Menu::getPid, menuId)
                .queryList()
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        menuIdList.add(menuId);
        return this.removeByIds(menuIdList);
    }

    @Override
    public List<MenuDto> listMenus(ConditionVo conditionVo) {
        List<Menu> menuList = this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVo.getKeywords()), Menu::getName, conditionVo.getKeywords())
                .queryList();

        Map<Integer, List<Menu>> childrenMenuMap = this.mapChildrenMenu(menuList);
        List<MenuDto> menuDtoList = this.listCatalog(menuList)
                .stream()
                .map(catalog -> {
                    List<MenuDto> children = BeanUtils.bean2Bean(childrenMenuMap.remove(catalog.getId()), MenuDto.class)
                            .stream()
                            .sorted(Comparator.comparing(MenuDto::getOrderNum))
                            .collect(Collectors.toList());

                    MenuDto menuDto = BeanUtils.bean2Bean(catalog, MenuDto.class);
                    menuDto.setChildren(children);
                    return menuDto;
                })
                .sorted(Comparator.comparing(MenuDto::getOrderNum))
                .collect(Collectors.toList());

        // 若还有菜单未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMenuMap)) {
            List<Menu> childrenMenuList = new ArrayList<>();
            childrenMenuMap.values().forEach(childrenMenuList::addAll);

            List<MenuDto> childrenMenuDtoList = childrenMenuList.stream()
                    .map(menu -> BeanUtils.bean2Bean(menu, MenuDto.class))
                    .sorted(Comparator.comparing(MenuDto::getOrderNum))
                    .collect(Collectors.toList());

            menuDtoList.addAll(childrenMenuDtoList);
        }
        return menuDtoList;
    }

    @Override
    public List<UserMenuDto> listUserMenus() {
        List<Menu> menuList = this.getBaseMapper().listMenusByUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        List<Menu> catalogList = this.listCatalog(menuList);
        Map<Integer, List<Menu>> childrenMenuMap = this.mapChildrenMenu(menuList);
        return this.convertUserMenuList(catalogList, childrenMenuMap);
    }

    @Override
    public List<LabelOptionDto> listMenuOptions() {
        List<Menu> menuList = this.beginQuery()
                .select(Menu::getId, Menu::getName, Menu::getPid, Menu::getOrderNum)
                .queryList();

        Map<Integer, List<Menu>> childrenMenuMap = this.mapChildrenMenu(menuList);
        return this.listCatalog(menuList)
                .stream()
                .map(catalog -> {
                    List<LabelOptionDto> children = Optional.ofNullable(childrenMenuMap.get(catalog.getId()))
                            .orElse(new ArrayList<>(0))
                            .stream()
                            .sorted(Comparator.comparing(Menu::getOrderNum))
                            .map(menu -> LabelOptionDto.builder()
                                    .id(menu.getId())
                                    .label(menu.getName())
                                    .build())
                            .collect(Collectors.toList());

                    return LabelOptionDto.builder()
                            .id(catalog.getId())
                            .label(catalog.getName())
                            .children(children)
                            .build();
                }).collect(Collectors.toList());
    }

    private List<Menu> listCatalog(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> Objects.isNull(menu.getPid()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    private Map<Integer, List<Menu>> mapChildrenMenu(List<Menu> menuList) {
        return menuList.stream()
                .filter(menu -> Objects.nonNull(menu.getPid()))
                .collect(Collectors.groupingBy(Menu::getPid));
    }

    private List<UserMenuDto> convertUserMenuList(List<Menu> catalogList, Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream()
                .map(catalog -> {
                    UserMenuDto userMenuDto;
                    List<UserMenuDto> children;

                    List<Menu> childrenMenuList = childrenMap.get(catalog.getId());
                    if (CollectionUtils.isNotEmpty(childrenMenuList)) {
                        // 多级菜单处理
                        userMenuDto = BeanUtils.bean2Bean(catalog, UserMenuDto.class);
                        children = childrenMenuList.stream()
                                .sorted(Comparator.comparing(Menu::getOrderNum))
                                .map(menu -> {
                                    UserMenuDto dto = BeanUtils.bean2Bean(menu, UserMenuDto.class);
                                    dto.setHidden(menu.getIsHidden().equals(TRUE));
                                    return dto;
                                })
                                .collect(Collectors.toList());
                    } else {
                        // 一级菜单处理
                        userMenuDto = UserMenuDto.builder()
                                .path(catalog.getPath())
                                .component(COMPONENT)
                                .build();

                        children = CollectionUtil.newArrayList(UserMenuDto.builder()
                                .path("")
                                .name(catalog.getName())
                                .icon(catalog.getIcon())
                                .component(catalog.getComponent())
                                .build());
                    }

                    userMenuDto.setHidden(catalog.getIsHidden().equals(TRUE));
                    userMenuDto.setChildren(children);
                    return userMenuDto;
                }).collect(Collectors.toList());
    }
}
