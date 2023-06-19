package org.boson.handler;

import org.boson.domain.Result;
import org.boson.domain.dto.UserInfoDto;
import org.boson.domain.po.UserAuth;
import org.boson.service.UserAuthService;
import org.boson.util.BeanUtils;
import org.boson.util.IOUtils;
import org.boson.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户登录成功处理器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired
    private UserAuthService userAuthService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        UserInfoDto userInfoDto = BeanUtils.bean2Bean(UserUtils.getLoginUser(), UserInfoDto.class);
        IOUtils.writeJSON(httpServletResponse, Result.ok(userInfoDto));
        updateUserInfo();
    }

    /**
     * 更新用户信息
     */
    @Async
    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .ipAddress(UserUtils.getLoginUser().getIpAddress())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime())
                .build();
        this.userAuthService.updateById(userAuth);
    }
}
