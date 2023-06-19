package org.boson.handler;

import org.boson.domain.Result;
import org.boson.enums.StatusCodeEnum;
import org.boson.util.IOUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户权限不足处理器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        IOUtils.writeJSON(httpServletResponse, Result.fail(StatusCodeEnum.AUTHORIZED));
    }
}
