package org.boson.domain;

import org.boson.enums.StatusCodeEnum;
import lombok.Data;
import org.boson.exception.BizException;


/**
 * 响应结果实体
 *
 * @author ShenXiaoYu
 * @since 0.0.1
 */
@Data
public class Result<T> {

    /**
     * 响应状态
     */
    private Boolean flag;

    /**
     * 响应编码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static <T> Result<T> ok(T data) {
        return Result.restResult(true, data, StatusCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(T data, String message) {
        return Result.restResult(true, data, StatusCodeEnum.SUCCESS.getCode(), message);
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return Result.restResult(false, null, code, message);
    }

    public static <T> Result<T> fail(StatusCodeEnum statusCodeEnum) {
        return Result.restResult(false, null, statusCodeEnum);
    }

    public static <T> Result<T> fail(String message) {
        return Result.fail(StatusCodeEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Throwable e) {
        return Result.fail(e.getMessage());
    }

    public static <T> Result<T> fail(BizException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    public static <T> Result<T> fail() {
        return Result.fail(StatusCodeEnum.FAIL);
    }

    public static <T> Result<T> check(boolean condition) {
        return condition ? Result.ok() : Result.fail();
    }

    public static <T> Result<T> check(boolean condition, String message) {
        return condition ? Result.ok() : Result.fail(message);
    }

    private static <T> Result<T> restResult(boolean flag, T data, Integer code, String message) {
        Result<T> result = new Result<>();
        result.setFlag(flag);
        result.setData(data);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    private static <T> Result<T> restResult(boolean flag, T data, StatusCodeEnum statusCodeEnum) {
        return Result.restResult(flag, data, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }
}
