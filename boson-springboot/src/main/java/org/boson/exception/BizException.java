package org.boson.exception;

import org.boson.enums.StatusCodeEnum;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    public BizException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BizException(String message) {
        this(StatusCodeEnum.FAIL.getCode(), message);
    }

    public BizException(StatusCodeEnum statusCodeEnum) {
        this(statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }
}
