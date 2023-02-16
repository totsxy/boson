package org.boson.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.boson.domain.dto.LabelOptionDTO;
import org.boson.domain.vo.ConditionVO;
import org.boson.mapper.MenuMapper;
import org.boson.mapper.RoleMenuMapper;
import org.boson.domain.dto.MenuDTO;
import org.boson.domain.dto.UserMenuDTO;
import org.boson.domain.po.Menu;
import org.boson.domain.po.RoleMenu;
import org.boson.exception.BizException;
import org.boson.domain.vo.MenuVO;
import org.boson.service.MenuService;
import org.boson.service.RoleMenuService;
import org.boson.support.mybatisplus.service.BaseServiceImpl;
import org.boson.util.BeanCopyUtils;
import org.boson.util.BeanUtils;
import org.boson.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.boson.constant.CommonConst.*;

/**
 * 菜单服务
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    private final RoleMenuService roleMenuService;

    public MenuServiceImpl(RoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO) {
        // 查询菜单数据
//        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
//                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Menu::getName, conditionVO.getKeywords()));

        List<Menu> menuList = this.beginQuery()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), Menu::getName, conditionVO.getKeywords())
                .queryList();

        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);

        // 组装目录菜单数据
        List<MenuDTO> menuDTOList = listCatalog(menuList).stream()
                .map(it -> {
                    MenuDTO menuDTO = BeanUtils.bean2Bean(it, MenuDTO.class);
                    List<MenuDTO> children = BeanUtils.bean2Bean(childrenMap.remove(it.getId()), MenuDTO.class).stream()
                            .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                            .collect(Collectors.toList());
                    menuDTO.setChildren(children);
                    return menuDTO;
                }).sorted(Comparator.comparing(MenuDTO::getOrderNum)).collect(Collectors.toList());

        // 若还有菜单未取出则拼接
        if (CollectionUtils.isNotEmpty(childrenMap)) {
            List<Menu> childrenList = CollectionUtil.newArrayList();
            childrenMap.values().forEach(childrenList::addAll);

            List<MenuDTO> childrenDTOList = childrenList.stream()
                    .map(it -> BeanUtils.bean2Bean(it, MenuDTO.class))
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTOList.addAll(childrenDTOList);
        }
        return menuDTOList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean saveOrUpdateMenu(MenuVO menuVO) {
        Menu menu = BeanUtils.bean2Bean(menuVO, Menu.class);
        return this.saveOrUpdate(menu);
    }

    @Override
    public Boolean deleteMenu(Integer menuId) {
        // 查询是否有角色关联
//        Integer count = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
//                .eq(RoleMenu::getMenuId, menuId));

        int count = roleMenuService.beginQuery()
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
        return menuIdList.size() == menuMapper.deleteBatchIds(menuIdList);
    }

    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        // 查询菜单数据
        List<Menu> menuList = menuMapper.selectList(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getPid, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 组装目录菜单数据
        return catalogList.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .label(menu.getName())
                                .build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getName())
                    .children(list)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserMenuDTO> listUserMenus() {
        // 查询用户菜单信息
        List<Menu> menuList = menuMapper.listMenusByUserInfoId(UserUtils.getLoginUser().getUserInfoId());
        // 获取目录列表
        List<Menu> catalogList = listCatalog(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMap = getMenuMap(menuList);
        // 转换前端菜单格式
        return convertUserMenuList(catalogList, childrenMap);
    }

    /**
     * 获取目录列表
     *
     * @param menuList 菜单列表
     * @return 目录列表
     */
    private List<Menu> listCatalog(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getPid()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    /**
     * 获取目录下菜单列表
     *
     * @param menuList 菜单列表
     * @return 目录下的菜单列表
     */
    private Map<Integer, List<Menu>> getMenuMap(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getPid()))
                .collect(Collectors.groupingBy(Menu::getPid));
    }

    /**
     * 转换用户菜单格式
     *
     * @param catalogList 目录
     * @param childrenMap 子菜单
     */
    private List<UserMenuDTO> convertUserMenuList(List<Menu> catalogList, Map<Integer, List<Menu>> childrenMap) {
        return catalogList.stream().map(item -> {
            // 获取目录
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();
            // 获取目录下的子菜单
            List<Menu> children = childrenMap.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                // 多级菜单处理
                userMenuDTO = BeanCopyUtils.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuDTO dto = BeanCopyUtils.copyObject(menu, UserMenuDTO.class);
                            dto.setHidden(menu.getIsHidden().equals(TRUE));
                            return dto;
                        })
                        .collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(COMPONENT);
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent())
                        .build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
    }

}
