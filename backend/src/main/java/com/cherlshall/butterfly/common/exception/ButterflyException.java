package com.cherlshall.butterfly.common.exception;

public class ButterflyException extends RuntimeException {

    private String msg;

    public ButterflyException() {
        this.msg = "server error";
    }

    public ButterflyException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
