package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.boson.domain.po.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 菜单表Mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户信息id查询菜单
     *
     * @param userInfoId 用户信息id
     * @return 菜单列表
     */
    List<Menu> listMenusByUserInfoId(Integer userInfoId);
}
