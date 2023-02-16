package org.boson.strategy;

import org.boson.domain.dto.UserInfoDto;

/**
 * 第三方登录策略
 *
 * @author yezhiqiu
 * @date 2021/07/28
 */
public interface SocialLoginStrategy {

    /**
     * 登录
     *
     * @param data 数据
     * @return {@link UserInfoDto} 用户信息
     */
    UserInfoDto login(String data);

}
