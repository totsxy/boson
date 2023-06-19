package org.boson.handler;

import org.boson.annotation.AccessLimit;
import org.boson.domain.Result;
import org.boson.service.RedisService;
import org.boson.util.IOUtils;
import org.boson.util.IpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 限流处理器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Log4j2
public class WebSecurityHandler implements HandlerInterceptor {

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        // 如果请求输入方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);

            if (accessLimit != null) {
                long seconds = accessLimit.seconds();
                int maxCount = accessLimit.maxCount();
                String key = IpUtils.getIpAddress(httpServletRequest) + hm.getMethod().getName();

                try {
                    // 从redis中获取用户自增1后的访问次数
                    long q = redisService.incrExpire(key, seconds);
                    if (q > maxCount) {
                        IOUtils.writeJSON(httpServletResponse, Result.fail("请求过于频繁，请稍候再试"));
                        log.warn(key + "请求次数超过每" + seconds + "秒" + maxCount + "次");
                        return false;
                    }
                    return true;
                } catch (RedisConnectionFailureException e) {
                    log.warn("redis错误: " + e.getMessage());
                    return false;
                }
            }
        }
        return true;
    }
}
