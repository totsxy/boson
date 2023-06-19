package org.boson.mapper;

import org.boson.domain.po.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;


/**
 * 用户信息表mapper
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Repository
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
