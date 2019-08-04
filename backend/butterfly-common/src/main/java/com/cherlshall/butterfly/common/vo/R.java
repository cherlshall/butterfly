package com.cherlshall.butterfly.common.vo;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    private static final String CODE = "code";
    private static final String MSG = "msg";
    private static final String DATA = "data";

    private R() {
    }

    private R(int code) {
        put(CODE, code);
    }

    private R(int code, String msg) {
        put(CODE, code);
        put(MSG, msg);
    }

    public static R error() {
        return error(Code.SERVER_ERROR);
    }

    public static R error(String msg) {
        return new R(Code.SERVER_ERROR.code(), msg);
    }

    public static R error(Code code) {
        return new R(code.code(), code.msg());
    }

    public static R error(Code code, String msg) {
        return new R(code.code(), msg);
    }

    public static R ok(Object data) {
        R r = new R(Code.OK.code());
        r.put(DATA, data);
        return r;
    }

    public static R ok() {
        return new R(Code.OK.code(), Code.OK.msg());
    }

    public R data(Object data) {
        this.put(DATA, data);
        return this;
    }

    public R msg(String msg) {
        this.put(MSG, msg);
        return this;
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
