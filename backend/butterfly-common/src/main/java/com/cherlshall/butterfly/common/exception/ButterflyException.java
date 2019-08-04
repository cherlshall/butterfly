package com.cherlshall.butterfly.common.exception;

import com.cherlshall.butterfly.common.vo.Code;

public class ButterflyException extends RuntimeException {

    private Code code;
    private String msg;

    public ButterflyException() {
        super(Code.SERVER_ERROR.msg());
        this.code = Code.SERVER_ERROR;
        this.msg = Code.SERVER_ERROR.msg();
    }

    public ButterflyException(String msg) {
        super(msg);
        this.code = Code.SERVER_ERROR;
        this.msg = msg;
    }

    public ButterflyException(Code code) {
        super(code.msg());
        this.code = code;
        this.msg = code.msg();
    }

    public ButterflyException(Code code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public Code code() {
        return code;
    }

    public String msg() {
        return msg;
    }
}
