package com.zz.messagepush.common.domain;

import com.zz.messagepush.common.enums.RespStatusEnum;
import lombok.Data;

/**
 * @author DELL
 */
@Data
public class ResponseResult<T> {

    protected String code;

    protected String msg;

    protected T data;

    public ResponseResult() {
    }


    public ResponseResult(String code, String msg, T data) {
        this.msg = msg;
        this.data = data;
        this.code = code;
    }
    public static <T> ResponseResult<T> success() {
        return new ResponseResult(RespStatusEnum.SUCCESS.getCode(), "ok", true);
    }

    public static <T> ResponseResult<T> success(String msg) {
        return new ResponseResult(RespStatusEnum.SUCCESS.getCode(), msg, true);
    }

    public static <T> ResponseResult<T> success(String msg, T data) {
        return new ResponseResult<>(RespStatusEnum.SUCCESS.getCode(), msg, data);
    }

    public static <T> ResponseResult<T> success(String code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }

    public static <T> ResponseResult<T> fail(String msg) {
        return new ResponseResult(RespStatusEnum.FAIL.getCode(), msg, true);
    }

    public static <T> ResponseResult<T> fail(String msg, T data) {
        return new ResponseResult<>(RespStatusEnum.FAIL.getCode(), msg, data);
    }

    public static <T> ResponseResult<T> fail(String code, String msg, T data) {
        return new ResponseResult<>(code, msg, data);
    }
}
