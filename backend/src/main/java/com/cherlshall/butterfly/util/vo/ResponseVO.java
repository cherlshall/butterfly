package com.cherlshall.butterfly.util.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseVO<T> {

    private Boolean success;
    private T data;
    private String msg;
    private int code;

    public static <T> ResponseVO<T> newInstance() {
        return new ResponseVO<>();
    }

    public static <T> ResponseVO<T> ofSuccess(T data) {
        return new ResponseVO<T>().setSuccess(true).setData(data);
    }

    public static <T> ResponseVO<T> ofFailure(String msg) {
        return new ResponseVO<T>().setSuccess(false).setMsg(msg);
    }
}
