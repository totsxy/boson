package org.boson.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.boson.domain.dto.RoleDto;
import org.boson.domain.dto.ResourceRoleDto;
import org.boson.domain.po.Role;
import org.apache.ibatis.annotations.Param;
import org.boson.domain.vo.ConditionVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色表mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 分页查询角色列表
     *
     * @param current     页码
     * @param size        条数
     * @param conditionVo 查询条件
     * @return 角色列表
     */
    List<RoleDto> pageRoles(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVo conditionVo);

    /**
     * 根据用户信息id查询角色列表
     *
     * @param userInfoId 用户信息id
     * @return 角色标签
     */
    List<String> listRolesByUserInfoId(Integer userInfoId);

    /**
     * 查看资源角色列表
     *
     * @return 资源角色列表
     */
    List<ResourceRoleDto> listResourceRoles();
}
