package org.boson.handler;

import org.boson.domain.Result;
import org.boson.exception.BizException;
import lombok.extern.log4j.Log4j2;
import org.boson.enums.StatusCodeEnum;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;


/**
 * 全局异常处理器
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 **/
@Log4j2
@RestControllerAdvice
public class ControllerAdviceHandler {

    /**
     * 处理业务异常
     *
     * @param e 业务异常对象
     * @return 接口异常信息
     */
    @ExceptionHandler(BizException.class)
    public Result<?> handle(BizException e) {
        return Result.fail(e);
    }

    /**
     * 处理参数校验异常
     *
     * @param e 参数校验异常对象
     * @return 接口异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handle(MethodArgumentNotValidException e) {
        return Result.fail(StatusCodeEnum.VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 系统异常对象
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> handle(Exception e) {
        return Result.fail(StatusCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }
}
