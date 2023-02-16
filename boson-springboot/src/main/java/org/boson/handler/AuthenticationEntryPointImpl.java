package org.boson.handler;

import org.boson.domain.Result;
import org.boson.enums.StatusCodeEnum;
import org.boson.util.HttpUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户未登录处理
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        HttpUtils.writeJSON(httpServletResponse, Result.fail(StatusCodeEnum.NO_LOGIN));
    }
}
