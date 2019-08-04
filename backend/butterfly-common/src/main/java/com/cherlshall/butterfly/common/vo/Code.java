package com.cherlshall.butterfly.common.vo;

public enum Code {

    OK(200, "ok"),
    BAD_REQUEST(400, "params error"),
    UNAUTH(401, "not logged in or permission denied"),
    SERVER_ERROR(500, "server error");

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private final int code;
    private final String msg;

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
