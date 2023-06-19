package org.boson.strategy.context;

import org.boson.domain.dto.UserInfoDto;
import org.boson.enums.LoginTypeEnum;
import org.boson.strategy.SocialLoginStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 * 第三方登录策略上下文
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Service
public class SocialLoginStrategyContext {

    @Autowired
    private Map<String, SocialLoginStrategy> socialLoginStrategyMap;

    /**
     * 执行第三方登录策略
     *
     * @param loginTypeEnum 登录枚举类型
     * @param data          数据
     * @return {@link UserInfoDto} 用户信息
     */
    public UserInfoDto executeLoginStrategy(LoginTypeEnum loginTypeEnum, String data) {
        return this.socialLoginStrategyMap.get(loginTypeEnum.getStrategy()).login(data);
    }
}
