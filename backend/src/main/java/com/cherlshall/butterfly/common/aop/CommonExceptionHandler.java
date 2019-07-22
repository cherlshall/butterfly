package com.cherlshall.butterfly.common.aop;

import com.cherlshall.butterfly.common.exception.ButterflyException;
import com.cherlshall.butterfly.common.vo.ResponseVO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(ButterflyException.class)
    public ResponseVO<Void> handleButterflyException(ButterflyException e) {
        return ResponseVO.ofFailure(e.getMsg());
    }

    @ExceptionHandler(Exception.class)
    public ResponseVO<Void> handleException(Exception e) {
        e.printStackTrace();
        return ResponseVO.ofFailure("server error");
    }
}
