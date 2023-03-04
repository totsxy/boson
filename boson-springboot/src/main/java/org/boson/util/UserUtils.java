package org.boson.util;

import org.boson.domain.dto.UserDetailDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * 用户工具类
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return 用户登录信息
     */
    public static UserDetailDto getLoginUser() {
        return (UserDetailDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
