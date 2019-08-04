package com.cherlshall.butterfly.common.aop;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.Code;
import com.cherlshall.butterfly.common.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(ButterflyException.class)
    public R handleButterflyException(ButterflyException e) {
        return R.error(e.code(), e.msg());
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        e.printStackTrace();
        return R.error(Code.SERVER_ERROR);
    }
}
