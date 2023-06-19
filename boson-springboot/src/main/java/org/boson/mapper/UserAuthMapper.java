package org.boson.mapper;

import org.boson.domain.dto.UserBackDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.boson.domain.po.UserAuth;
import org.boson.domain.vo.ConditionVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户账户表mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 分页查询后台用户列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 查询条件
     * @return {@link List<UserBackDto>} 后台用户列表
     */
    List<UserBackDto> pageUsers(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVo condition);

    /**
     * 查询后台用户数量
     *
     * @param condition 查询条件
     * @return 后台用户数量
     */
    Integer countUser(@Param("condition") ConditionVo condition);
}
