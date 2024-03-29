package org.boson.handler;

import org.boson.domain.Result;
import org.boson.util.IOUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户登录失败处理器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AuthenticationFailHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        IOUtils.writeJSON(httpServletResponse, Result.fail(e));
    }
}
