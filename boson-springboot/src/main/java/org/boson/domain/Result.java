package org.boson.domain;

import org.boson.enums.StatusCodeEnum;
import lombok.Data;


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

    public static <T> Result<T> ok() {
        return restResult(true, null, StatusCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(T data) {
        return restResult(true, data, StatusCodeEnum.SUCCESS);
    }

    public static <T> Result<T> ok(T data, String message) {
        return restResult(true, data, StatusCodeEnum.SUCCESS.getCode(), message);
    }

    public static <T> Result<T> fail() {
        return restResult(false, null, StatusCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(false, data, StatusCodeEnum.FAIL);
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(false, data, StatusCodeEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(String message) {
        return restResult(false, null, StatusCodeEnum.FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }

    public static <T> Result<T> fail(StatusCodeEnum statusCodeEnum) {
        return restResult(false, null, statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> Result<T> check(boolean condition) {
        return condition ? Result.ok() : Result.fail();
    }

    public static <T> Result<T> check(boolean condition, String failMessage) {
        return condition ? Result.ok() : Result.fail(failMessage);
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
