package org.boson.handler;

import org.boson.domain.Result;
import org.boson.domain.dto.UserInfoDTO;
import org.boson.domain.po.UserAuth;
import org.boson.service.UserAuthService;
import org.boson.util.BeanUtils;
import org.boson.util.HttpUtils;
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
 * 登录成功处理
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final UserAuthService userAuthService;

    @Autowired
    public AuthenticationSuccessHandlerImpl(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        UserInfoDTO userInfoDTO = BeanUtils.bean2Bean(UserUtils.getLoginUser(), UserInfoDTO.class);
        HttpUtils.writeJSON(httpServletResponse, Result.ok(userInfoDTO));
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
        userAuthService.updateById(userAuth);
    }
}
