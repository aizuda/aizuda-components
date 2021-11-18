package com.taptap.ratelimiter;

import com.aizuda.limiter.exception.RateLimitException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class TestExceptionHandler {

    /**
     * 全局捕获限流器异常处理
     */
    @ExceptionHandler(value = RateLimitException.class)
    @ResponseBody
    public String exceptionHandler(RateLimitException e) {
        return e.getMessage();
    }
}
