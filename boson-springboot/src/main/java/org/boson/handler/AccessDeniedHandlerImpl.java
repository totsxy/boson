package org.boson.handler;

import org.boson.domain.Result;
import org.boson.util.HttpUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户权限处理
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        HttpUtils.writeJSON(httpServletResponse, Result.fail("权限不足"));
    }
}
